package pt.unl.fct.iadi.novaevents.model

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.util.Optional

data class Event(
    val id: Long,
    val clubId: Club,
    @field:NotBlank
    val name: String,
    val date: LocalDate,
    val location: Optional<String>,
    val type: EventType,
    val description: Optional<String>,
    )
