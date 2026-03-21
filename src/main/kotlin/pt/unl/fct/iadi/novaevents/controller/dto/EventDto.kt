package pt.unl.fct.iadi.novaevents.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import pt.unl.fct.iadi.novaevents.model.EventType
import java.time.LocalDate
import java.util.Optional

data class EventDto(
    var id: Long = -1,
    var club: String = "",
    var clubId: Long = -1,
    @field:NotBlank(message = "Name is required")
    var name: String = "",
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @field:NotNull(message = "Date is required")
    var date: LocalDate = LocalDate.now(),
    var location: String? = null,
    var type: EventTypeDTO = EventTypeDTO.OTHER,
    var description: String? = null,
)
