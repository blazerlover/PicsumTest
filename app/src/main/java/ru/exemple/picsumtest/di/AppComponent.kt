package ru.exemple.picsumtest.di

import dagger.Component
import ru.exemple.picsumtest.ui.AuthorizationFragment
import ru.exemple.picsumtest.ui.PicturesFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, OkHttpClientModule::class, FragmentModule::class])
interface AppComponent {

    fun injectPictureFragment(picturesFragment: PicturesFragment)

    fun injectAuthorizationFragment(authorizationFragment: AuthorizationFragment)

}