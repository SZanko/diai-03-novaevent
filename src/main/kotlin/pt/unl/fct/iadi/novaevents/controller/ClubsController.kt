package pt.unl.fct.iadi.novaevents.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import pt.unl.fct.iadi.novaevents.service.ClubService
import java.util.logging.Logger
@Controller
@RequestMapping("/clubs")
class ClubsController(
    val clubService: ClubService,
) {
    companion object {
        private val log = LoggerFactory.getLogger(ClubsController::class.java)
    }

    @GetMapping
    fun listClubs(model: Model): String {
        log.info("Get all clubs page")

        model.addAttribute("clubs", clubService.findAll())

        return "clubs"
    }

    @GetMapping("/{id}")
    fun getClubDetails(@PathVariable id: Long, model: Model): String {

        log.info("Get club ${id} details")

        return "club";
    }
}