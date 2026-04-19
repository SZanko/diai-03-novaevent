package pt.unl.fct.iadi.novaevents.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClientException
import pt.unl.fct.iadi.novaevents.client.OpenWeatherMapClient
import pt.unl.fct.iadi.novaevents.client.OpenWeatherMapCondition
import pt.unl.fct.iadi.novaevents.client.OpenWeatherMapResponse

class TestWeatherService {
    @Test
    fun `rain condition returns true`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherMapResponse(listOf(OpenWeatherMapCondition("Rain", "light rain")))), "test-key")

        assertEquals(true, service.isRaining("Lisbon"))
    }

    @Test
    fun `clear condition returns false`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherMapResponse(listOf(OpenWeatherMapCondition("Clear", "clear sky")))), "test-key")

        assertEquals(false, service.isRaining("Lisbon"))
    }

    @Test
    fun `unavailable weather returns null`() {
        val service = WeatherService(FakeWeatherClient(exception = RestClientException("offline")), "test-key")

        assertNull(service.isRaining("Lisbon"))
    }

    @Test
    fun `blank location returns null`() {
        val service = WeatherService(FakeWeatherClient(OpenWeatherMapResponse()), "test-key")

        assertNull(service.isRaining(" "))
    }

    private class FakeWeatherClient(
        private val response: OpenWeatherMapResponse = OpenWeatherMapResponse(),
        private val exception: RuntimeException? = null,
    ) : OpenWeatherMapClient {
        override fun currentWeather(location: String, apiKey: String): OpenWeatherMapResponse {
            exception?.let { throw it }
            return response
        }
    }
}

