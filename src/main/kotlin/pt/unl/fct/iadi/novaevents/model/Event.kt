package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.Optional

@Entity
data class Event(
    @field:Min(value = 1, message = "Event ID must be greater than or equal to 1")
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @field:Min(value = 1, message = "Event ID must be greater than or equal to 1")
    val clubId: Long,
    @field:NotBlank(message = "Name is required")
    @field:Column(unique=true)
    val name: String,
    @field:NotNull(message = "Date is required")
    var date: LocalDate,
    val location: Optional<String>,
    @field:NotNull(message = "Event type is required")
    var type: EventType,
    val description: Optional<String>,
    )
