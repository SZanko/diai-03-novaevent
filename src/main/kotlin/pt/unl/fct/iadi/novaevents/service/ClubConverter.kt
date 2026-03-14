package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.controller.dto.ClubDto
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.ClubCategorie

@Component
class ClubConverter: Converter<ClubDto, Club> {
    override fun convertDtoToModel(dto: ClubDto): Club {
        return Club(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            category = ClubCategorie.valueOf(dto.category),
        )
    }

    override fun convertModelToDto(model: Club): ClubDto {
        return ClubDto(
            id = model.id,
            name = model.name,
            description = model.description,
            category = model.category.name
        )
    }

}
