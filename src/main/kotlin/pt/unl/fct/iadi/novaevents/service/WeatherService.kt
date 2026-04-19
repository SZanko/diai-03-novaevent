package pt.unl.fct.iadi.novaevents.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import pt.unl.fct.iadi.novaevents.client.OpenWeatherMapClient

@Service
class WeatherService(
    private val openWeatherMapClient: OpenWeatherMapClient,
    @Value("\${weather.api.key}") apiKey: String,
) {
    private val apiKey: String = apiKey

    fun isRaining(location: String): Boolean? {
        val normalizedLocation = location.trim()
        if (normalizedLocation.isEmpty()) {
            return null
        }

        return try {
            val response = openWeatherMapClient.currentWeather(normalizedLocation, apiKey)
            val condition = response.weather.firstOrNull() ?: return null
            val main = condition.main.trim()
            val description = condition.description?.trim().orEmpty()
            main.equals("Rain", ignoreCase = true) ||
                main.equals("Drizzle", ignoreCase = true) ||
                main.equals("Thunderstorm", ignoreCase = true) ||
                description.contains("rain", ignoreCase = true)
        } catch (_: RestClientException) {
            null
        } catch (_: RuntimeException) {
            null
        }
    }
}
