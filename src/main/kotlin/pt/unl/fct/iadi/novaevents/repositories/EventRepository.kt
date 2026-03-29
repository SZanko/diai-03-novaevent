package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Event
import java.time.LocalDate
import java.util.Optional

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.type WHERE e.club.id = :clubId")
    fun findEventsByClubId(@Param("clubId") clubId: Long): List<Event>

    fun findEventByName(name: String): Optional<Event>

    @Query("SELECT e FROM Event e WHERE e.id = :eventId AND e.club.id = :clubId")
    fun findByIdAndClubId(
        @Param("eventId") eventId: Long,
        @Param("clubId") clubId: Long
    ): Optional<Event>

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.type WHERE e.type = :type")
    fun findByType(@Param("type") type: String?): List<Event>

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.type WHERE e.club.id = :clubId")
    fun findByClubId(@Param("clubId") clubId: Long): List<Event>

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.type WHERE e.date >= :from AND e.date <= :to")
    fun findByDateRange(@Param("from") from: LocalDate, @Param("to") to: LocalDate): List<Event>
}