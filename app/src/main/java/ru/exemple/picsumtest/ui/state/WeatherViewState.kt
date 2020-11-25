package ru.exemple.picsumtest.ui.state

import ru.exemple.picsumtest.ui.row.WeatherRow

data class WeatherViewState(
    val fetchStatus: FetchStatus,
    val weather: WeatherRow?
)