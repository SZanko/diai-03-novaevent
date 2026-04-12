package pt.unl.fct.iadi.novaevents.service

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.RoleName
import pt.unl.fct.iadi.novaevents.repositories.AppUserRepository

@Service
class DatabaseUserDetailsManager(
    private val appUserRepository: AppUserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsManager {

    override fun createUser(user: UserDetails) {
        if (userExists(user.username)) {
            throw IllegalArgumentException("User ${user.username} already exists")
        }

        appUserRepository.save(toEntity(user))
    }

    override fun updateUser(user: UserDetails) {
        val existingUser = findDomainUser(user.username)
        existingUser.password = encodeIfNeeded(user.password)
        existingUser.replaceRoles(toRoleNames(user))
        appUserRepository.save(existingUser)
    }

    override fun deleteUser(username: String) {
        appUserRepository.findByUsername(username).ifPresent(appUserRepository::delete)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        val currentAuthentication = SecurityContextHolder.getContext().authentication
            ?: throw AccessDeniedException("No authenticated user is available")

        val existingUser = findDomainUser(currentAuthentication.name)
        if (!passwordEncoder.matches(oldPassword, existingUser.password)) {
            throw AccessDeniedException("Current password is invalid")
        }

        existingUser.password = passwordEncoder.encode(newPassword)
        appUserRepository.save(existingUser)
    }

    override fun userExists(username: String): Boolean {
        return appUserRepository.existsByUsername(username)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return toUserDetails(findDomainUser(username))
    }

    fun findDomainUser(username: String): AppUser {
        return appUserRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User $username was not found") }
    }

    private fun toUserDetails(user: AppUser): UserDetails {
        return User.withUsername(user.username)
            .password(user.password)
            .authorities(*user.roles.map { it.role.name }.toTypedArray())
            .build()
    }

    private fun toEntity(user: UserDetails): AppUser {
        val entity = AppUser(
            username = user.username,
            password = encodeIfNeeded(user.password),
        )
        entity.replaceRoles(toRoleNames(user))
        return entity
    }

    private fun encodeIfNeeded(password: String): String {
        return if (password.startsWith("$2")) password else passwordEncoder.encode(password)
    }

    private fun toRoleNames(user: UserDetails): List<RoleName> {
        return user.authorities.map { authority -> RoleName.valueOf(authority.authority) }
    }
}
