package pt.unl.fct.iadi.novaevents.client

data class OpenWeatherMapResponse(
    val weather: List<OpenWeatherMapCondition> = emptyList(),
)

data class OpenWeatherMapCondition(
    val main: String = "",
    val description: String? = null,
)

