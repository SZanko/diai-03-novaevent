package pt.unl.fct.iadi.novaevents.service

import jakarta.validation.Validator
import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.EventDto
import pt.unl.fct.iadi.novaevents.controller.dto.EventTypeDTO
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.service.exceptions.ClubNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventNotFoundException
import pt.unl.fct.iadi.novaevents.service.exceptions.EventValidationException
import java.time.LocalDate
import java.util.Optional

@Service
class EventsService(
    private val validator: Validator,
    // Because there is no db copy and paste the code
    private val clubs: List<Club> = listOf(
        Club(id = 1, name = "Chess Club", description = "Chess Club description", ClubCategorie.SPORTS),
        Club(
            id = 2,
            name = "Robotics Club",
            description = "The Robotics Club is the place to turn ideas into machines",
            ClubCategorie.TECHNOLOGY
        ),
        Club(id = 3, name = "Photography Club", description = "description", ClubCategorie.ARTS),
        Club(id = 4, name = "Hiking & Outdoors Club", description = "description", ClubCategorie.SPORTS),
        Club(id = 5, name = "Film Society", description = "description", ClubCategorie.SPORTS),
    ),
    private val events: MutableList<Event> = mutableListOf(
        Event(
            id = 1,
            clubId = 1,
            //name = "Beginner&#39;s Chess Workshop",
            name = "Beginner's Chess Workshop",
            LocalDate.of(2026, 3, 10),
            Optional.of<String>("Room A101"),
            type = EventType.WORKSHOP,
            Optional.of("Beginner Workshop")
        ),
        Event(
            id = 2,
            clubId = 1,
            name = "Spring Chess Tournament",
            date = LocalDate.of(2026, 4, 5),
            location = Optional.of("Main Hall"),
            type = EventType.COMPETITION,
            description = Optional.of("Spring Tournament")
        ),
        Event(
            id = 3,
            clubId = 1,
            name = "Advanced Openings Talk",
            date = LocalDate.of(2026, 5, 20),
            location = Optional.of("Room A101"),
            type = EventType.TALK,
            description = Optional.of("Advanced Openings Talk")
        ),
        Event(
            id = 4,
            clubId = 2,
            name = "Arduino Intro Workshop",
            date = LocalDate.of(2026, 3, 15),
            location = Optional.of("Engineering Lab 2"),
            type = EventType.WORKSHOP,
            description = Optional.of("Arduino Introduction Workshop")
        ),
        Event(
            id = 5,
            clubId = 2,
            name = "RoboCup Preparation Meeting",
            date = LocalDate.of(2026, 3, 28),
            location = Optional.of("Engineering Lab 1"),
            type = EventType.MEETING,
            description = Optional.of("RoboCup Preparation Meeting")
        ),
        Event(
            id = 6,
            clubId = 2,
            name = "Sensor Integration Talk",
            date = LocalDate.of(2026, 4, 22),
            location = Optional.of("Auditorium B"),
            type = EventType.TALK,
            description = Optional.of("Sensor Integration Talk")
        ),
        Event(
            id = 7,
            clubId = 2,
            name = "Regional Robotics Competition",
            date = LocalDate.of(2026, 6, 1),
            location = Optional.of("Sports Hall"),
            type = EventType.COMPETITION,
            description = Optional.of("Regional Robotics Competition")
        ),
        Event(
            id = 8,
            clubId = 3,
            name = "Night Photography Workshop",
            date = LocalDate.of(2026, 3, 22),
            location = Optional.of("Campus Rooftop"),
            type = EventType.WORKSHOP,
            description = Optional.of("Night Photography Workshop")
        ),
        Event(
            id = 9,
            clubId = 3,
            name = "Portrait Photography Talk",
            date = LocalDate.of(2026, 4, 14),
            location = Optional.of("Arts Studio 3"),
            type = EventType.TALK,
            description = Optional.of("Portrait Photography Talk")
        ),
        Event(
            id = 10,
            clubId = 3,
            name = "Photo Walk & Social",
            date = LocalDate.of(2026, 5, 9),
            location = Optional.of("Main Entrance"),
            type = EventType.SOCIAL,
            description = Optional.of("Photo Walk and Social")
        ),
        Event(
            id = 11,
            clubId = 4,
            name = "Serra da Arrábida Hike",
            date = LocalDate.of(2026, 3, 29),
            location = Optional.of("Bus Stop Central"),
            type = EventType.OTHER,
            description = Optional.of("Serra da Arrábida Hike")
        ),
        Event(
            id = 12,
            clubId = 4,
            name = "Trail Safety Workshop",
            date = LocalDate.of(2026, 4, 8),
            location = Optional.of("Room C205"),
            type = EventType.WORKSHOP,
            description = Optional.of("Trail Safety Workshop")
        ),
        Event(
            id = 13,
            clubId = 4,
            name = "Spring Camping Trip",
            date = LocalDate.of(2026, 5, 15),
            location = Optional.of("Bus Stop Central"),
            type = EventType.SOCIAL,
            description = Optional.of("Spring Camping Trip")
        ),
        Event(
            id = 14,
            clubId = 5,
            name = "Kubrick Retrospective Screening",
            date = LocalDate.of(2026, 3, 18),
            location = Optional.of("Cinema Room"),
            type = EventType.SOCIAL,
            description = Optional.of("Kubrick Retrospective Screening")
        ),
        Event(
            id = 15,
            clubId = 5,
            name = "Screenwriting Workshop",
            date = LocalDate.of(2026, 4, 30),
            location = Optional.of("Arts Studio 1"),
            type = EventType.WORKSHOP,
            description = Optional.of("Screenwriting Workshop")
        )
    )
) {
    private fun convertModelToDto(model: Event): EventDto {
        val club = clubs.find { it.id == model.clubId }?.name;
        if (club == null) {
            throw ClubNotFoundException()
        }
        return EventDto(
            id = model.id,
            clubId = model.clubId,
            club = club,
            name = model.name,
            date = model.date,
            location = model.location.orElse(null),
            type = EventTypeDTO.valueOf(model.type.name),
            description = model.description.orElse(null),
        )
    }

    private fun convertDtoToModel(dto: EventDto): Event {
        var id = dto.id;
        if (dto.id == (-1).toLong()) {
            id = clubs[clubs.size - 1].id + 1;
        }

        return Event(
            id = id,
            clubId = dto.clubId,
            name = dto.name,
            date = dto.date,
            location = Optional.ofNullable(dto.location),
            type = EventType.valueOf(dto.type.name),
            description = Optional.ofNullable(dto.description),
        )
    }

    private fun convertModelToDto(models: List<Event>): List<EventDto> {
        return models.map { convertModelToDto(it) }
    }

    fun getEvents(type: String?, club: String?, from: String?, to: String?): List<EventDto> {

        val fromDate = from?.let { LocalDate.parse(it) }
        val toDate = to?.let { LocalDate.parse(it) }

        val filtered = events.filter { event ->
            val matchesType = type == null || event.type.name == type
            val matchesClub = club == null || event.clubId.toString() == club
            val matchesFrom = fromDate == null || !event.date.isBefore(fromDate)
            val matchesTo = toDate == null || !event.date.isAfter(toDate)
            matchesType && matchesClub && matchesFrom && matchesTo
        }



        return convertModelToDto(filtered)
    }

    fun getEventByClubIdAndId(club: Long, eventId: Long): EventDto {
        events.find { it.clubId == club && it.id == eventId }?.let {
            return convertModelToDto(it)
        }
        throw EventNotFoundException()
    }

    fun saveEvent(event: EventDto): EventDto {
        val toBeSaved = convertDtoToModel(event)


        val violations = validator.validate(toBeSaved)
        if (violations.isNotEmpty()) {
            throw EventValidationException()
        }

        events.add(toBeSaved)


        return convertModelToDto(toBeSaved)
    }

    fun deleteEvent(club: Long, eventId: Long) {
        val event = events.find { it.clubId == club && it.id == eventId }
            ?: throw EventNotFoundException()

        events.remove(event)
    }

    fun updateEvent(eventId: Long, event: EventDto): EventDto? {
        return null
    }
}