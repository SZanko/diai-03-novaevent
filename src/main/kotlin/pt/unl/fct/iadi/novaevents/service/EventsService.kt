package pt.unl.fct.iadi.novaevents.service

import jakarta.validation.Validator
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDto
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.controller.dto.EventTypeDTO
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import pt.unl.fct.iadi.novaevents.service.exceptions.ClubNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventDuplicateNameException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventValidationException
import java.time.LocalDate
import java.util.Optional

@Service
class EventsService(
    private val validator: Validator,
    private val clubConverter: ClubConverter,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
) {
    private fun convertModelToDto(model: Event): EventDto {
        val club = clubRepository.findById(model.club?.id ?: -1L)
        if (club.isEmpty) {
            throw ClubNotFoundException()
        }
        return EventDto(
            id = model.id ?: 0,
            clubId = club.get().id!!,
            club = club.get().name,
            name = model.name,
            date = model.date,
            location = model.location,
            type = EventTypeDTO.valueOf(model.type.name),
            description = model.description,
        )
    }

    private fun convertDtoToModel(dto: EventDto): Event {

        val club = clubRepository.findById(dto.clubId)
        if (club.isEmpty) {
            throw ClubNotFoundException()
        }


        return Event(
            id = dto.id,
            club = club.get(),
            name = dto.name,
            date = dto.date!!,
            location = dto.location,
            type = EventType.valueOf(dto.type!!.name),
            description = dto.description,
        )
    }

    private fun convertModelToDto(models: List<Event>): List<EventDto> {
        return models.map { convertModelToDto(it) }
    }

    fun getEvents(type: String?, club: String?, from: String?, to: String?): List<EventDto> {

        val events = eventRepository.findAll() // I know it is not very efficient

        val fromDate = from?.let { LocalDate.parse(it) }
        val toDate = to?.let { LocalDate.parse(it) }

        val filtered = events.filter { event ->
            val matchesType = type == null || event.type.name == type
            val matchesClub = club == null || event.club?.id.toString() == club
            val matchesFrom = fromDate == null || !event.date.isBefore(fromDate)
            val matchesTo = toDate == null || !event.date.isAfter(toDate)
            matchesType && matchesClub && matchesFrom && matchesTo
        }



        return convertModelToDto(filtered)
    }

    fun getEventByClubIdAndId(club: Long, eventId: Long): EventDto {
        throw EventNotFoundException()
    }

    fun saveEvent(event: EventDto): EventDto {
        val toBeSaved = convertDtoToModel(event)

        val violations = validator.validate(toBeSaved)
        if (violations.isNotEmpty()) {
            throw EventValidationException()
        }
        val existingName = eventRepository.findEventByName(event.name)

        if (existingName.isPresent) {
            throw EventDuplicateNameException()
        }



        return convertModelToDto(
            eventRepository.save(toBeSaved)
        )
    }

    fun deleteEvent(club: Long, eventId: Long) {
        val toBeDeleted = clubRepository.findById(club);
        if (toBeDeleted.isEmpty) {
            throw EventNotFoundException()
        }
        clubRepository.deleteById(club)
    }

    fun updateEvent(eventId: Long, event: EventDto): EventDto? {
        val existingEvent = eventRepository.findById(eventId)
        if (existingEvent.isEmpty) throw EventNotFoundException()

        val events = eventRepository.findAll();

        val duplicate = events.any {
            it.id != eventId && it.name.equals(event.name, ignoreCase = true)
        }
        if (duplicate) throw EventDuplicateNameException()

        var updatedEvent = existingEvent.get()

        val existingClub = clubRepository.findById(event.clubId)
        if (existingClub.isEmpty) {
            throw ClubNotFoundException()
        }


        updatedEvent = Event(
            id = eventId,
            club = existingClub.get(),
            name = event.name,
            date = event.date!!,
            location = event.location,
            type = EventType.valueOf(event.type!!.name),
            description = event.description
        )



        return convertModelToDto(updatedEvent)
    }

    fun getEventById(eventId: Long): EventDto {

        val result = eventRepository.findById(eventId)
        if (result.isEmpty) {
            throw EventNotFoundException()
        }

        return convertModelToDto(result.get())
    }

    fun findByClub(clubId: Long): List<EventDto> {

        val result = eventRepository.findEventsByClubId(clubId)

        if (result.isEmpty()) {
            throw ClubNotFoundException()
        }

        return convertModelToDto(result)
    }

}