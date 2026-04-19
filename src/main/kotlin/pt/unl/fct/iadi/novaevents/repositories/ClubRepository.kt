package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Club
import java.util.Optional

@Repository
interface ClubRepository : JpaRepository<Club, Long> {

    @Query("SELECT DISTINCT c FROM Club c LEFT JOIN FETCH c.events")
    override fun findAll(): List<Club>

    @Query("SELECT DISTINCT c FROM Club c LEFT JOIN FETCH c.events")
    fun findAllWithEvents(): List<Club>

    @Query("SELECT DISTINCT c FROM Club c LEFT JOIN FETCH c.events WHERE c.id = :id")
    override fun findById(id: Long): Optional<Club>
}
