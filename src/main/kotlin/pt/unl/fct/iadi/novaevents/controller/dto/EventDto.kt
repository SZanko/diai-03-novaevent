package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import pt.unl.fct.iadi.novaevents.model.Club
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate
import java.util.Optional

data class EventDto(
    val club: String,
    @field:NotBlank
    val name: String,
    val date: LocalDate,
    val location: Optional<String>,
    val type: EventType,
)
