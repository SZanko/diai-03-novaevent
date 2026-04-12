package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ModelAndView {
        return ModelAndView("404").apply {
            status = HttpStatus.NOT_FOUND
        }
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(ex: AccessDeniedException): ModelAndView {
        return ModelAndView("403").apply {
            status = HttpStatus.FORBIDDEN
        }
    }
}
