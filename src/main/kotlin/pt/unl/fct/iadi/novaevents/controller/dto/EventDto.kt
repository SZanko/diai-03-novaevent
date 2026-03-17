package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate
import java.util.Optional

data class EventDto(
    val id: Long,
    val club: String,
    val clubId: Long,
    @field:NotBlank
    val name: String,
    val date: LocalDate,
    val location: Optional<String>,
    val type: EventType,
    val description: Optional<String>,
)
