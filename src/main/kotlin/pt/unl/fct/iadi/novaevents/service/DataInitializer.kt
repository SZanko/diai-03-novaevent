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
        val club1 = Club(
            //id = 1
            name = "Chess Club",
            description = "Chess Club description",
            category = ClubCategorie.SPORTS
        )
        val club2 = Club(
            // id = 2,
            name = "Robotics Club",
            description = "The Robotics Club is the place to turn ideas into machines",
            category = ClubCategorie.TECHNOLOGY

        )
        val club3 = Club(
            //id = 3,
            name = "Photography Club", description = "description",
            category = ClubCategorie.ARTS
        )
        val club4 = Club(
            //id = 4,
            name = "Hiking & Outdoors Club", description = "description",

            category = ClubCategorie.SPORTS
        )
        val club5 =Club(
            //id = 5,
            name = "Film Society", description = "description",

            category = ClubCategorie.SPORTS
        )
        val clubs: List<Club> = listOf(
            club1,
            club2,
            club3,
            club4,
            club5
        )


        val event1 =Event(
            //id = 1,
            club = clubs[0],
            //name = "Beginner&#39;s Chess Workshop",
            name = "Beginner's Chess Workshop",
            date = LocalDate.of(2026, 3, 10),
            location = "Room A101",
            type = EventType.WORKSHOP,
            description = "Beginner Workshop"
        )
        val event2 =Event(
            //id = 2,
            club = clubs[0],
            name = "Spring Chess Tournament",
            date = LocalDate.of(2026, 4, 5),
            location = "Main Hall",
            type = EventType.COMPETITION,
            description = "Spring Tournament"
        )
        val event3 = Event(
            //id = 3,
            club = clubs[0],
            name = "Advanced Openings Talk",
            date = LocalDate.of(2026, 5, 20),
            location = "Room A101",
            type = EventType.TALK,
            description = "Advanced Openings Talk"
        )
        val event4 = Event(
            //id = 4,
            club = clubs[1],
            name = "Arduino Intro Workshop",
            date = LocalDate.of(2026, 3, 15),
            location = "Engineering Lab 2",
            type = EventType.WORKSHOP,
            description = "Arduino Introduction Workshop"
        )
        val event5 = Event(
            //id = 5,
            club = clubs[1],
            name = "RoboCup Preparation Meeting",
            date = LocalDate.of(2026, 3, 28),
            location = "Engineering Lab 1",
            type = EventType.MEETING,
            description = "RoboCup Preparation Meeting"
        )
        val event6 = Event(
            //id = 6,
            club = clubs[1],
            name = "Sensor Integration Talk",
            date = LocalDate.of(2026, 4, 22),
            location = "Auditorium B",
            type = EventType.TALK,
            description = "Sensor Integration Talk"
        )
        val event7 = Event(
            //id = 7,
            club = clubs[1],
            name = "Regional Robotics Competition",
            date = LocalDate.of(2026, 6, 1),
            location = "Sports Hall",
            type = EventType.COMPETITION,
            description = "Regional Robotics Competition"
        )
        val event8 = Event(
            //id = 8,
            club = clubs[2],
            name = "Night Photography Workshop",
            date = LocalDate.of(2026, 3, 22),
            location = "Campus Rooftop",
            type = EventType.WORKSHOP,
            description = "Night Photography Workshop"
        )
        val event9 = Event(
            //id = 9,
            club = clubs[2],
            name = "Portrait Photography Talk",
            date = LocalDate.of(2026, 4, 14),
            location = "Arts Studio 3",
            type = EventType.TALK,
            description = "Portrait Photography Talk"
        )
        val event10 = Event(
            //id = 10,
            club = clubs[2],
            name = "Photo Walk & Social",
            date = LocalDate.of(2026, 5, 9),
            location = "Main Entrance",
            type = EventType.SOCIAL,
            description = "Photo Walk and Social"
        )
        val event11 = Event(
            //id = 11,
            club = clubs[3],
            name = "Serra da Arrábida Hike",
            date = LocalDate.of(2026, 3, 29),
            location = "Bus Stop Central",
            type = EventType.OTHER,
            description = "Serra da Arrábida Hike"
        )
        val event12 = Event(
            //id = 12,
            club = clubs[3],
            name = "Trail Safety Workshop",
            date = LocalDate.of(2026, 4, 8),
            location = "Room C205",
            type = EventType.WORKSHOP,
            description = "Trail Safety Workshop"
        )
        val event13 = Event(
            //id = 13,
            club = clubs[3],
            name = "Spring Camping Trip",
            date = LocalDate.of(2026, 5, 15),
            location = "Bus Stop Central",
            type = EventType.SOCIAL,
            description = "Spring Camping Trip"
        )
        val event14 = Event(
            //id = 14,
            club = clubs[4],
            name = "Kubrick Retrospective Screening",
            date = LocalDate.of(2026, 3, 18),
            location = "Cinema Room",
            type = EventType.SOCIAL,
            description = "Kubrick Retrospective Screening"
        )
        val event15 = Event(
            //id = 15,
            club = clubs[4],
            name = "Screenwriting Workshop",
            date = LocalDate.of(2026, 4, 30),
            location = "Arts Studio 1",
            type = EventType.WORKSHOP,
            description = "Screenwriting Workshop"
        )

        val events: MutableList<Event> = mutableListOf(
            event1,
            event2,
            event3,
            event4,
            event5,
            event6,
            event7,
            event8,
            event9,
            event10,
            event11,
            event12,
            event13,
            event14,
            event15,
        )


        club1.events.add(event1)
        club1.events.add(event2)
        club1.events.add(event3)

        club2.events.add(event4)
        club2.events.add(event5)
        club2.events.add(event6)
        club2.events.add(event7)

        club3.events.add(event8)
        club3.events.add(event9)
        club3.events.add(event10)

        club4.events.add(event11)
        club4.events.add(event12)
        club4.events.add(event13)

        club5.events.add(event14)
        club5.events.add(event15)

        clubRepository.saveAll(clubs)

        eventRepository.saveAll(events)
    }
}