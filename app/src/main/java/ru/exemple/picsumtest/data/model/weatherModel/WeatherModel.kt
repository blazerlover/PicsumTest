package ru.exemple.picsumtest.data.model.weatherModel

data class WeatherModel(
    val name: String,
    val weather: List<Weather>,
    val main: Main
)