package pt.unl.fct.iadi.novaevents.service

import jakarta.validation.Validator
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.controller.dto.EventTypeDTO
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repositories.AppUserRepository
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import pt.unl.fct.iadi.novaevents.service.exceptions.ClubNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventDuplicateNameException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventValidationException
import java.time.LocalDate

@Service
class EventsService(
    private val validator: Validator,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
    private val appUserRepository: AppUserRepository,
) {
    private fun convertModelToDto(model: Event): EventDto {
        val club = model.club ?: throw ClubNotFoundException()
        return EventDto(
            id = model.id ?: 0,
            clubId = club.id ?: 0,
            club = club.name,
            name = model.name,
            date = model.date,
            location = model.location,
            type = EventTypeDTO.valueOf(model.type.name),
            description = model.description,
            ownerUsername = model.owner?.username ?: "",
        )
    }

    private fun convertModelsToDtos(models: List<Event>): List<EventDto> {
        return models.map(::convertModelToDto)
    }

    private fun findClub(clubId: Long): Club {
        return clubRepository.findById(clubId)
            .orElseThrow { ClubNotFoundException() }
    }

    private fun findOwner(username: String): AppUser {
        return appUserRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User $username was not found") }
    }

    private fun validateEvent(event: Event) {
        val violations = validator.validate(event)
        if (violations.isNotEmpty()) {
            throw EventValidationException()
        }
    }

    fun getEvents(type: String?, club: String?, from: String?, to: String?): List<EventDto> {
        val events = eventRepository.findAll()
        val fromDate = from?.let(LocalDate::parse)
        val toDate = to?.let(LocalDate::parse)

        val filtered = events.filter { event ->
            val matchesType = type == null || event.type.name == type
            val matchesClub = club == null || event.club?.id.toString() == club
            val matchesFrom = fromDate == null || !event.date.isBefore(fromDate)
            val matchesTo = toDate == null || !event.date.isAfter(toDate)
            matchesType && matchesClub && matchesFrom && matchesTo
        }

        return convertModelsToDtos(filtered)
    }

    fun getEventByClubIdAndId(clubId: Long, eventId: Long): EventDto {
        val event = eventRepository.findByIdAndClubId(eventId, clubId)
            .orElseThrow { EventNotFoundException() }
        return convertModelToDto(event)
    }

    fun saveEvent(event: EventDto, ownerUsername: String): EventDto {
        if (eventRepository.existsByNameIgnoreCase(event.name)) {
            throw EventDuplicateNameException()
        }

        val toBeSaved = Event(
            club = findClub(event.clubId),
            name = event.name,
            date = event.date!!,
            location = event.location,
            type = EventType.valueOf(event.type!!.name),
            description = event.description,
            owner = findOwner(ownerUsername),
        )

        validateEvent(toBeSaved)
        return convertModelToDto(eventRepository.save(toBeSaved))
    }

    fun deleteEvent(clubId: Long, eventId: Long) {
        val toBeDeleted = eventRepository.findByIdAndClubId(eventId, clubId)
            .orElseThrow { EventNotFoundException() }
        eventRepository.delete(toBeDeleted)
    }

    fun updateEvent(eventId: Long, event: EventDto): EventDto {
        val existingEvent = eventRepository.findById(eventId)
            .orElseThrow { EventNotFoundException() }

        if (eventRepository.existsByNameIgnoreCaseAndIdNot(event.name, eventId)) {
            throw EventDuplicateNameException()
        }

        val updatedEvent = Event(
            id = eventId,
            club = findClub(event.clubId),
            name = event.name,
            date = event.date!!,
            location = event.location,
            type = EventType.valueOf(event.type!!.name),
            description = event.description,
            owner = existingEvent.owner,
        )

        validateEvent(updatedEvent)
        return convertModelToDto(eventRepository.save(updatedEvent))
    }

    fun getEventById(eventId: Long): EventDto {
        val result = eventRepository.findById(eventId)
            .orElseThrow { EventNotFoundException() }
        return convertModelToDto(result)
    }

    fun findByClub(clubId: Long): List<EventDto> {
        if (!clubRepository.existsById(clubId)) {
            throw ClubNotFoundException()
        }

        return convertModelsToDtos(eventRepository.findEventsByClubId(clubId))
    }
}
