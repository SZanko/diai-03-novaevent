package pt.unl.fct.iadi.novaevents.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import pt.unl.fct.iadi.novaevents.client.OpenWeatherMapClient

@Configuration
class WeatherClientConfig {
    @Bean
    fun openWeatherMapClient(restClientBuilder: RestClient.Builder): OpenWeatherMapClient {
        val restClient = restClientBuilder
            .baseUrl("https://api.openweathermap.org")
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(OpenWeatherMapClient::class.java)
    }
}

