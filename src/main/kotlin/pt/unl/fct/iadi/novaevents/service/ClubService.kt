package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDto
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie
import pt.unl.fct.iadi.novaevents.repositories.ClubRepository
import pt.unl.fct.iadi.novaevents.repositories.EventRepository
import pt.unl.fct.iadi.novaevents.service.exceptions.ClubNotFoundException

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubConverter: ClubConverter
) {


    fun findAll(): List<ClubDto> {

        val clubs = clubRepository.findAll()

        return clubConverter.convertModelToDto(clubs)
    }

    fun findById(id: Long): ClubDto {

        val result = clubRepository.findById(id)
        if(result.isEmpty){
            throw ClubNotFoundException()
        }

        return clubConverter.convertModelToDto(result.get())
    }
}