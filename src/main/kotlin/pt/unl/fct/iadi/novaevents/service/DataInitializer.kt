package pt.unl.fct.iadi.novaevents.service

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pt.unl.fct.iadi.novaevents.model.AppUser
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.model.Event
import pt.unl.fct.iadi.novaevents.model.EventType
import pt.unl.fct.iadi.novaevents.model.RoleName
import pt.unl.fct.iadi.novaevents.repositories.AppUserRepository
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import java.time.LocalDate

@Configuration
class DataInitializer(
    private val appUserRepository: AppUserRepository,
    private val clubRepository: ClubRepository,
    private val passwordEncoder: PasswordEncoder,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        seedUserIfMissing("alice", "password123", RoleName.ROLE_EDITOR)
        seedUserIfMissing("bob", "password123", RoleName.ROLE_EDITOR)
        seedUserIfMissing("charlie", "password123", RoleName.ROLE_ADMIN)

        if (clubRepository.count() > 0) {
            return
        }

        val alice = appUserRepository.findByUsername("alice").orElseThrow()
        val bob = appUserRepository.findByUsername("bob").orElseThrow()

        val club1 = Club(
            name = "Chess Club",
            description = "Chess Club description",
            category = ClubCategorie.SPORTS,
        )
        val club2 = Club(
            name = "Robotics Club",
            description = "The Robotics Club is the place to turn ideas into machines",
            category = ClubCategorie.TECHNOLOGY,
        )
        val club3 = Club(
            name = "Photography Club",
            description = "description",
            category = ClubCategorie.ARTS,
        )
        val club4 = Club(
            name = "Hiking & Outdoors Club",
            description = "description",
            category = ClubCategorie.SPORTS,
        )
        val club5 = Club(
            name = "Film Society",
            description = "description",
            category = ClubCategorie.SPORTS,
        )
        val clubs = listOf(club1, club2, club3, club4, club5)

        val event1 = Event(
            club = clubs[0],
            name = "Beginner's Chess Workshop",
            date = LocalDate.of(2026, 3, 10),
            location = "Room A101",
            type = EventType.WORKSHOP,
            description = "Beginner Workshop",
            owner = alice,
        )
        val event2 = Event(
            club = clubs[0],
            name = "Spring Chess Tournament",
            date = LocalDate.of(2026, 4, 5),
            location = "Main Hall",
            type = EventType.COMPETITION,
            description = "Spring Tournament",
            owner = alice,
        )
        val event3 = Event(
            club = clubs[0],
            name = "Advanced Openings Talk",
            date = LocalDate.of(2026, 5, 20),
            location = "Room A101",
            type = EventType.TALK,
            description = "Advanced Openings Talk",
            owner = alice,
        )
        val event4 = Event(
            club = clubs[1],
            name = "Arduino Intro Workshop",
            date = LocalDate.of(2026, 3, 15),
            location = "Engineering Lab 2",
            type = EventType.WORKSHOP,
            description = "Arduino Introduction Workshop",
            owner = bob,
        )
        val event5 = Event(
            club = clubs[1],
            name = "RoboCup Preparation Meeting",
            date = LocalDate.of(2026, 3, 28),
            location = "Engineering Lab 1",
            type = EventType.MEETING,
            description = "RoboCup Preparation Meeting",
            owner = bob,
        )
        val event6 = Event(
            club = clubs[1],
            name = "Sensor Integration Talk",
            date = LocalDate.of(2026, 4, 22),
            location = "Auditorium B",
            type = EventType.TALK,
            description = "Sensor Integration Talk",
            owner = bob,
        )
        val event7 = Event(
            club = clubs[1],
            name = "Regional Robotics Competition",
            date = LocalDate.of(2026, 6, 1),
            location = "Sports Hall",
            type = EventType.COMPETITION,
            description = "Regional Robotics Competition",
            owner = bob,
        )
        val event8 = Event(
            club = clubs[2],
            name = "Night Photography Workshop",
            date = LocalDate.of(2026, 3, 22),
            location = "Campus Rooftop",
            type = EventType.WORKSHOP,
            description = "Night Photography Workshop",
            owner = alice,
        )
        val event9 = Event(
            club = clubs[2],
            name = "Portrait Photography Talk",
            date = LocalDate.of(2026, 4, 14),
            location = "Arts Studio 3",
            type = EventType.TALK,
            description = "Portrait Photography Talk",
            owner = alice,
        )
        val event10 = Event(
            club = clubs[2],
            name = "Photo Walk & Social",
            date = LocalDate.of(2026, 5, 9),
            location = "Main Entrance",
            type = EventType.SOCIAL,
            description = "Photo Walk and Social",
            owner = alice,
        )
        val event11 = Event(
            club = clubs[3],
            name = "Serra da Arrabida Hike",
            date = LocalDate.of(2026, 3, 29),
            location = "Bus Stop Central",
            type = EventType.OTHER,
            description = "Serra da Arrabida Hike",
            owner = bob,
        )
        val event12 = Event(
            club = clubs[3],
            name = "Trail Safety Workshop",
            date = LocalDate.of(2026, 4, 8),
            location = "Room C205",
            type = EventType.WORKSHOP,
            description = "Trail Safety Workshop",
            owner = bob,
        )
        val event13 = Event(
            club = clubs[3],
            name = "Spring Camping Trip",
            date = LocalDate.of(2026, 5, 15),
            location = "Bus Stop Central",
            type = EventType.SOCIAL,
            description = "Spring Camping Trip",
            owner = bob,
        )
        val event14 = Event(
            club = clubs[4],
            name = "Kubrick Retrospective Screening",
            date = LocalDate.of(2026, 3, 18),
            location = "Cinema Room",
            type = EventType.SOCIAL,
            description = "Kubrick Retrospective Screening",
            owner = bob,
        )
        val event15 = Event(
            club = clubs[4],
            name = "Screenwriting Workshop",
            date = LocalDate.of(2026, 4, 30),
            location = "Arts Studio 1",
            type = EventType.WORKSHOP,
            description = "Screenwriting Workshop",
            owner = bob,
        )

        club1.events.addAll(listOf(event1, event2, event3))
        club2.events.addAll(listOf(event4, event5, event6, event7))
        club3.events.addAll(listOf(event8, event9, event10))
        club4.events.addAll(listOf(event11, event12, event13))
        club5.events.addAll(listOf(event14, event15))

        clubRepository.saveAll(clubs)
    }

    private fun seedUserIfMissing(username: String, rawPassword: String, role: RoleName) {
        if (appUserRepository.existsByUsername(username)) {
            return
        }

        val user = AppUser(
            username = username,
            password = passwordEncoder.encode(rawPassword),
        )
        user.replaceRoles(listOf(role))
        appUserRepository.save(user)
    }
}
