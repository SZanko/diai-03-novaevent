package pt.unl.fct.iadi.novaevents.client

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("/data/2.5/weather")
interface OpenWeatherMapClient {
    @GetExchange
    fun currentWeather(
        @RequestParam("q") location: String,
        @RequestParam("appid") apiKey: String,
    ): OpenWeatherMapResponse
}

