package pt.unl.fct.iadi.novaevents.service.exceptions

class EventOutdoorRuleException(
    val field: String,
    message: String,
) : RuntimeException(message)
