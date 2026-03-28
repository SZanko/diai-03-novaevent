package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate

data class EventDto(
    var id: Long = -1,
    var club: String = "",
    var clubId: Long = -1,
    @field:NotBlank(message = "Name is required")
    var name: String = "",
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull(message = "Date is required")
    var date: LocalDate? = null,
    var location: String? = null,
    @field:NotNull(message = "Event type is required")
    var type: EventTypeDTO? = null,
    var description: String? = null,
)
