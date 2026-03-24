package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
data class Club(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val name: String,
    @field:Column(length = 2000)
    val description: String,
    @field:OneToMany(cascade = [(CascadeType.ALL)])
    val events: MutableList<Event>,
    val category: ClubCategorie
)

