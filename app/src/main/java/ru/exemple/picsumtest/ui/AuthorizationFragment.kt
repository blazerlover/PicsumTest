package ru.exemple.picsumtest.ui

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.exemple.picsumtest.App
import ru.exemple.picsumtest.R
import ru.exemple.picsumtest.ui.row.WeatherRow
import ru.exemple.picsumtest.ui.state.FetchStatus.*
import ru.exemple.picsumtest.ui.state.WeatherViewState
import ru.exemple.picsumtest.utils.DEGREE_UNIT
import ru.exemple.picsumtest.viewModel.AuthorizationViewModel
import javax.inject.Inject


class AuthorizationFragment : Fragment() {

    @Inject
    lateinit var authorizationViewModelFactory: AuthorizationViewModel.AuthorizationViewModelFactory
    private lateinit var authorizationViewModel: AuthorizationViewModel

    private lateinit var rootView: View
    private lateinit var etPassword: EditText

    private var passVisibility: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as App).getComponent().injectAuthorizationFragment(this)
        authorizationViewModel = ViewModelProvider(
                this,
                authorizationViewModelFactory
        ).get(AuthorizationViewModel::class.java)
        authorizationViewModel.weatherLiveData.observe(this) {
            updateDisplayState(it)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_authorization, container, false)
        val etLogin: EditText = rootView.findViewById(R.id.fragment_authorization__etLogin)
        etPassword = rootView.findViewById(R.id.fragment_authorization__etPassword)
        etPassword.transformationMethod = PasswordTransformationMethod()
        val ivShowPass: ImageView = rootView.findViewById(R.id.fragment_authorization__ivShowPass)
        ivShowPass.setOnClickListener {
            setPasswordVisibility(passVisibility)
            passVisibility = !passVisibility
        }
        val btLogin: Button = rootView.findViewById(R.id.fragment_authorization__btLogin)
        btLogin.setOnClickListener {
            authorizationViewModel.onLoginButtonClick(
                    etLogin.text.toString(),
                    etPassword.text.toString()
            )
        }

        return rootView
    }

    private fun updateDisplayState(weatherViewState: WeatherViewState) {
        when (weatherViewState.fetchStatus) {
            is Fetching -> showLoading()
            is Fetched -> {
                hideLoading()
                updateData(weatherViewState.weather!!)
            }
            is NotFetched -> {
                hideLoading()
                showUnsuccessfulMessage()
            }
            is InvalidPassword -> passwordInvalid()
        }
    }

    private fun setPasswordVisibility(isVisible: Boolean) {
        if (isVisible) {
            etPassword.transformationMethod = PasswordTransformationMethod()
        } else {
            etPassword.transformationMethod = null
        }
    }

    private fun passwordInvalid() {
        Snackbar
                .make(rootView, "Please check that your username and password are correct", Snackbar.LENGTH_LONG)
                .show()
    }

    private fun showLoading() {

    }

    private fun hideLoading() {

    }

    private fun updateData(weather: WeatherRow) {
        val snackbar: Snackbar = Snackbar.make(
                rootView,
                "Погода в Петербурге:\n" +
                        "${weather.cloud}.\n" +
                        "Температура воздуха: ${weather.temp} + $DEGREE_UNIT.\n" +
                        "Влажность: ${weather.humidity}.)",
                Snackbar.LENGTH_LONG
        )
        val snackbarView = snackbar.view
        val snackTextView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackTextView.maxLines = 4
        snackbar.show()
    }

    private fun showUnsuccessfulMessage() {
        Snackbar
                .make(rootView, "Data was fetch unsuccessfully", Snackbar.LENGTH_LONG)
                .show()
    }
}