package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Entity
data class Event(
    @field:Min(value = 1, message = "Event ID must be greater than or equal to 1")
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", referencedColumnName = "id")
    var club: Club? = null,
    @field:NotBlank(message = "Name is required")
    @field:Column(unique = true)
    val name: String,
    @field:NotNull(message = "Date is required")
    var date: LocalDate,
    val location: String?,
    @field:NotNull(message = "Event type is required")
    var type: EventType,
    val description: String?,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    var owner: AppUser? = null,
) {
    constructor() : this(null, null, "", LocalDate.now(), null, EventType.OTHER, null, null)
}
