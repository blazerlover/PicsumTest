package ru.exemple.picsumtest.data.network

import retrofit2.Response
import ru.exemple.picsumtest.data.model.PictureModel
import ru.exemple.picsumtest.data.model.weatherModel.WeatherModel
import rx.Observable
import javax.inject.Inject

class RetrofitService @Inject constructor(var retrofit: Retrofit) {

    fun getPictures(page: String, limit: String): Observable<Response<List<PictureModel>>> {
        return retrofit.api.getPictures(page, limit)
    }

    fun getWeather(): Observable<Response<WeatherModel>> {
        return retrofit.api.getWeather(
            "https://api.openweathermap.org/data/2.5/weather?",
            "498817",
            "metric",
            "ru"
        )
    }
}