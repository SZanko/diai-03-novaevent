package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate
import java.util.Optional

data class EventDto(
    var id: Long = -1,
    var club: String = "",
    var clubId: Long = -1,
    @field:NotBlank
    var name: String = "",
    var date: LocalDate = LocalDate.now(),
    var location: String? = null,
    var type: EventTypeDTO = EventTypeDTO.OTHER,
    var description: String? = null,
)
