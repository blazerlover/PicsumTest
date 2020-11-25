package ru.exemple.picsumtest

import android.app.Application
import ru.exemple.picsumtest.di.AppComponent
import ru.exemple.picsumtest.di.AppModule
import ru.exemple.picsumtest.di.DaggerAppComponent

class App : Application() {

    companion object {
        private lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun getComponent(): AppComponent {
        return appComponent
    }
}