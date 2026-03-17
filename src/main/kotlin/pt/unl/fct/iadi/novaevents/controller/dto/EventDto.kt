package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate
import java.util.Optional

data class EventDto(
    val id: Long = -1,
    val club: String = "",
    var clubId: Long = -1,
    @field:NotBlank
    val name: String = "",
    val date: LocalDate = LocalDate.now(),
    val location: String? = null,
    val type: EventTypeDTO = EventTypeDTO.OTHER,
    val description: String? = null,
)
