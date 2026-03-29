package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Club

@Repository
interface ClubRepository : JpaRepository<Club, Long> {

    @Query("SELECT c FROM Club c LEFT JOIN FETCH c.events")
    override fun findAll(): List<Club>
}