package pt.unl.fct.iadi.novaevents

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import pt.unl.fct.iadi.novaevents.controller.dto.ErrorResponse
import kotlin.jvm.java

@RestControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
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