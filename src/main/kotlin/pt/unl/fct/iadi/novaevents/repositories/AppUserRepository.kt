package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.AppUser
import java.util.Optional

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = ["roles"])
    fun findByUsername(username: String): Optional<AppUser>

    fun existsByUsername(username: String): Boolean
}
