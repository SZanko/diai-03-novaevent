package pt.unl.fct.iadi.novaevents.controller.dto

data class ClubDto(
    val id : Long,
    val name: String,
    val description: String,
    val category: String,
    val eventCount: Int,
)
