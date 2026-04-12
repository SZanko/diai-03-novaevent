package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var username: String = "",
    @Column(nullable = false)
    var password: String = "",
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var roles: MutableList<UserRole> = mutableListOf(),
) {
    fun replaceRoles(roleNames: Collection<RoleName>) {
        roles.clear()
        roleNames.distinct().forEach { role ->
            roles.add(UserRole(user = this, role = role))
        }
    }
}
