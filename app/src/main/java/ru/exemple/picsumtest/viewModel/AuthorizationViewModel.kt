package ru.exemple.picsumtest.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.exemple.picsumtest.data.network.RetrofitService
import ru.exemple.picsumtest.ui.row.WeatherRow
import ru.exemple.picsumtest.ui.state.FetchStatus.*
import ru.exemple.picsumtest.ui.state.WeatherViewState
import ru.exemple.picsumtest.utils.EMAIL_REGEX
import ru.exemple.picsumtest.utils.PASSWORD_REGEX
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.regex.Pattern
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(var retrofitService: RetrofitService) :
        ViewModel() {

    private val lWeatherLiveData = MutableLiveData<WeatherViewState>()
    val weatherLiveData get() = lWeatherLiveData

    private var weather: WeatherRow? = null

    fun onLoginButtonClick(login: String, password: String) {
        if (passwordIsValid(login, password)) {
            getWeather()
        } else {
            passwordInvalid()
        }
    }

    private fun getWeather() {
        fetching()
        retrofitService.getWeather()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()!!.let { model ->
                            weather = WeatherRow(
                                    model.name,
                                    model.main.temp,
                                    model.weather[0].description,
                                    model.main.humidity
                            )
                        }
                        fetched()
                        Log.d("TAG", it.body().toString())
                    } else {
                        notFetched(it.message())
                        Log.d("TAG", it.message())
                    }
                }, {
                    notFetched(it.message!!)
                })
    }

    private fun passwordIsValid(login: String, password: String): Boolean {
        val loginMatcher = Pattern.compile(EMAIL_REGEX).matcher(login)
        val passwordMatcher = Pattern.compile(PASSWORD_REGEX).matcher(password)
        return (loginMatcher.matches() && passwordMatcher.matches())
    }

    private fun fetching() {
        lWeatherLiveData.postValue(WeatherViewState(Fetching, weather))
    }

    private fun fetched() {
        lWeatherLiveData.postValue(WeatherViewState(Fetched, weather))
    }

    private fun notFetched(message: String) {
        val notFetched = NotFetched.also {
            it.message = message
        }
        lWeatherLiveData.postValue(WeatherViewState(notFetched, weather))
    }

    private fun passwordInvalid() {
        lWeatherLiveData.postValue(WeatherViewState(InvalidPassword, weather))
    }

    @Suppress("UNCHECKED_CAST")
    class AuthorizationViewModelFactory(private val retrofitService: RetrofitService) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthorizationViewModel(retrofitService) as T
        }
    }
}