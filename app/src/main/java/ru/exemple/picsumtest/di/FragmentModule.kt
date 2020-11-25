package ru.exemple.picsumtest.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.exemple.picsumtest.data.network.RetrofitService
import ru.exemple.picsumtest.viewModel.AuthorizationViewModel
import ru.exemple.picsumtest.viewModel.PictureViewModel
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class FragmentModule {

    @Provides
    @Singleton
    fun providesPictureViewModelFactory(context: Context, retrofitService: RetrofitService): PictureViewModel.PictureViewModelFactory {
        return PictureViewModel.PictureViewModelFactory(context, retrofitService)
    }

    @Provides
    @Singleton
    fun providesAuthViewModelFactory(retrofitService: RetrofitService): AuthorizationViewModel.AuthorizationViewModelFactory {
        return AuthorizationViewModel.AuthorizationViewModelFactory(retrofitService)
    }
}