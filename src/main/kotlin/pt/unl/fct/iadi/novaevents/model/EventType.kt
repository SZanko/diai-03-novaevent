package pt.unl.fct.iadi.novaevents.model

import jakarta.persistence.Table



@Table(name = "event_type")
enum class EventType{
    WORKSHOP,
    TALK,
    COMPETITION,
    SOCIAL,
    MEETING,
    OTHER
}