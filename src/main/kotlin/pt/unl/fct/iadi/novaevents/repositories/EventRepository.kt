package pt.unl.fct.iadi.novaevents.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pt.unl.fct.iadi.novaevents.model.Event
import java.util.Optional

@Repository
interface EventRepository : JpaRepository<Event, Long> {

    fun findEventsByClubId(clubId: Long): List<Event>

    fun findEventByName(name: String): Optional<Event>
}