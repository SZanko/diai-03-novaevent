package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDto
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie

@Service
class ClubService(
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
        Club(id = 5, name = "Filme Society", description = "description", ClubCategorie.SPORTS),
    ),
    private val clubConverter: ClubConverter
) {


    fun findAll(): List<ClubDto> {

        return clubConverter.convertModelToDto(clubs)
    }

    fun findClubById(id: Long): ClubDto? {

        clubs.find { it.id == id }?.let {
            return clubConverter.convertModelToDto(it)
        }
        return null
    }
}