package pt.unl.fct.iadi.novaevents.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import pt.unl.fct.iadi.novaevents.controller.dto.ErrorResponse

@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ModelAndView {
        val mav = ModelAndView("404")
        mav.status = HttpStatus.NOT_FOUND
        return mav
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(throwable: Throwable): ResponseEntity<ErrorResponse> {
        val status = throwable::class.java
            .getAnnotation(ResponseStatus::class.java)
            ?.value ?: HttpStatus.INTERNAL_SERVER_ERROR

        return ResponseEntity
            .status(status)
            .body(ErrorResponse(throwable.message ?: "Unexpected error"))
    }
}