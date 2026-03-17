package pt.unl.fct.iadi.novaevents.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.Optional

data class Event(
    @field:Min(value = 1, message = "Event ID must be greater than or equal to 1")
    val id: Long,
    @field:Min(value = 1, message = "Event ID must be greater than or equal to 1")
    val clubId: Long,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val date: LocalDate,
    val location: Optional<String>,
    @field:NotNull
    val type: EventType,
    val description: Optional<String>,
    )
