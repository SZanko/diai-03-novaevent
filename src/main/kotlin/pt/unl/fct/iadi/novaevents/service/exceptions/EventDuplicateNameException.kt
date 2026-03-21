package pt.unl.fct.iadi.novaevents.service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class EventDuplicateNameException: RuntimeException("An event with this name already exists") {
}