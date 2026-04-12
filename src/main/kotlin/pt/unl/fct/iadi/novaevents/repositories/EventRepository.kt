package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Event
import java.util.Optional

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.owner")
    override fun findAll(): List<Event>

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.owner WHERE e.club.id = :clubId")
    fun findEventsByClubId(@Param("clubId") clubId: Long): List<Event>

    fun findEventByName(name: String): Optional<Event>

    fun existsByNameIgnoreCase(name: String): Boolean

    fun existsByNameIgnoreCaseAndIdNot(name: String, id: Long): Boolean

    fun existsByIdAndOwnerUsername(id: Long, username: String): Boolean

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.owner WHERE e.id = :eventId AND e.club.id = :clubId")
    fun findByIdAndClubId(
        @Param("eventId") eventId: Long,
        @Param("clubId") clubId: Long,
    ): Optional<Event>

    @Query("SELECT e FROM Event e JOIN FETCH e.club JOIN FETCH e.owner WHERE e.id = :eventId")
    override fun findById(@Param("eventId") eventId: Long): Optional<Event>
}
