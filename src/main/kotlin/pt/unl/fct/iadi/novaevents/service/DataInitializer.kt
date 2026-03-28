package pt.unl.fct.iadi.novaevents.service

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import java.time.LocalDate
import java.util.Optional

@Configuration
class DataInitializer(
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        val events: MutableList<Event> = mutableListOf(
            Event(
                id = 1,
                clubId = 1,
                //name = "Beginner&#39;s Chess Workshop",
                name = "Beginner's Chess Workshop",
                LocalDate.of(2026, 3, 10),
                "Room A101",
                type = EventType.WORKSHOP,
                "Beginner Workshop"
            ),
            Event(
                id = 2,
                clubId = 1,
                name = "Spring Chess Tournament",
                date = LocalDate.of(2026, 4, 5),
                location = "Main Hall",
                type = EventType.COMPETITION,
                description = "Spring Tournament"
            ),
            Event(
                id = 3,
                clubId = 1,
                name = "Advanced Openings Talk",
                date = LocalDate.of(2026, 5, 20),
                location = "Room A101",
                type = EventType.TALK,
                description = "Advanced Openings Talk"
            ),
            Event(
                id = 4,
                clubId = 2,
                name = "Arduino Intro Workshop",
                date = LocalDate.of(2026, 3, 15),
                location = "Engineering Lab 2",
                type = EventType.WORKSHOP,
                description = "Arduino Introduction Workshop"
            ),
            Event(
                id = 5,
                clubId = 2,
                name = "RoboCup Preparation Meeting",
                date = LocalDate.of(2026, 3, 28),
                location = "Engineering Lab 1",
                type = EventType.MEETING,
                description = "RoboCup Preparation Meeting"
            ),
            Event(
                id = 6,
                clubId = 2,
                name = "Sensor Integration Talk",
                date = LocalDate.of(2026, 4, 22),
                location = "Auditorium B",
                type = EventType.TALK,
                description = "Sensor Integration Talk"
            ),
            Event(
                id = 7,
                clubId = 2,
                name = "Regional Robotics Competition",
                date = LocalDate.of(2026, 6, 1),
                location = "Sports Hall",
                type = EventType.COMPETITION,
                description = "Regional Robotics Competition"
            ),
            Event(
                id = 8,
                clubId = 3,
                name = "Night Photography Workshop",
                date = LocalDate.of(2026, 3, 22),
                location = "Campus Rooftop",
                type = EventType.WORKSHOP,
                description = "Night Photography Workshop"
            ),
            Event(
                id = 9,
                clubId = 3,
                name = "Portrait Photography Talk",
                date = LocalDate.of(2026, 4, 14),
                location = "Arts Studio 3",
                type = EventType.TALK,
                description = "Portrait Photography Talk"
            ),
            Event(
                id = 10,
                clubId = 3,
                name = "Photo Walk & Social",
                date = LocalDate.of(2026, 5, 9),
                location = "Main Entrance",
                type = EventType.SOCIAL,
                description = "Photo Walk and Social"
            ),
            Event(
                id = 11,
                clubId = 4,
                name = "Serra da Arrábida Hike",
                date = LocalDate.of(2026, 3, 29),
                location = "Bus Stop Central",
                type = EventType.OTHER,
                description = "Serra da Arrábida Hike"
            ),
            Event(
                id = 12,
                clubId = 4,
                name = "Trail Safety Workshop",
                date = LocalDate.of(2026, 4, 8),
                location = "Room C205",
                type = EventType.WORKSHOP,
                description = "Trail Safety Workshop"
            ),
            Event(
                id = 13,
                clubId = 4,
                name = "Spring Camping Trip",
                date = LocalDate.of(2026, 5, 15),
                location = "Bus Stop Central",
                type = EventType.SOCIAL,
                description = "Spring Camping Trip"
            ),
            Event(
                id = 14,
                clubId = 5,
                name = "Kubrick Retrospective Screening",
                date = LocalDate.of(2026, 3, 18),
                location = "Cinema Room",
                type = EventType.SOCIAL,
                description = "Kubrick Retrospective Screening"
            ),
            Event(
                id = 15,
                clubId = 5,
                name = "Screenwriting Workshop",
                date = LocalDate.of(2026, 4, 30),
                location = "Arts Studio 1",
                type = EventType.WORKSHOP,
                description = "Screenwriting Workshop"
            )
        )

        val clubs: List<Club> = listOf(
            Club(
                id = 1,
                name = "Chess Club",
                description = "Chess Club description",
                events = events.filter { it.clubId == 1L } as MutableList<Event>,
                category = ClubCategorie.SPORTS),
            Club(
                id = 2,
                name = "Robotics Club",
                description = "The Robotics Club is the place to turn ideas into machines",
                events = events.filter { it.clubId == 2L } as MutableList<Event>,
                category = ClubCategorie.TECHNOLOGY

            ),
            Club(id = 3, name = "Photography Club", description = "description",
                events = events.filter { it.clubId == 3L } as MutableList<Event>,
                    ClubCategorie.ARTS),
            Club(id = 4, name = "Hiking & Outdoors Club", description = "description",
                events = events.filter { it.clubId == 4L } as MutableList<Event>,
                ClubCategorie.SPORTS),
            Club(id = 5, name = "Film Society", description = "description",
                events = events.filter { it.clubId == 5L } as MutableList<Event>,
                ClubCategorie.SPORTS),
        )

        eventRepository.saveAll(events)

        clubRepository.saveAll(clubs)
    }
}