package ru.exemple.picsumtest.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.exemple.picsumtest.data.network.RetrofitService
import ru.exemple.picsumtest.ui.row.PictureRow
import ru.exemple.picsumtest.ui.state.FetchStatus.*
import ru.exemple.picsumtest.ui.state.PicturesViewState
import ru.exemple.picsumtest.utils.REQUEST_BUNDLE_PIC
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PictureViewModel @Inject constructor(
        var context: Context,
        var retrofitService: RetrofitService
) : ViewModel() {

    private val lPicturesLiveData = MutableLiveData<PicturesViewState>()
    val picturesLiveData get() = lPicturesLiveData

    private val lAllPicturesLiveData = MutableLiveData<PicturesViewState>()
    val allPicturesLiveData get() = lAllPicturesLiveData

    private val picturesRowBundle = mutableListOf<PictureRow>()
    private val picturesRow = mutableListOf<PictureRow>()

    fun getPictures(page: Int) {
        fetching()
        retrofitService.getPictures(page.toString(), REQUEST_BUNDLE_PIC)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()!!.also { pictures ->
                            for (pic in pictures) {
                                PictureRow(pic.download_url, pic.author).run {
                                    picturesRowBundle.add(this)
                                    picturesRow.add(this)
                                }
                            }
                        }
                        fetched()
                    } else {
                        notFetched(it.message())
                    }
                }, {
                    notFetched(it.message!!)
                })
        picturesRowBundle.clear()
    }

    fun getAllPictures() {
        lAllPicturesLiveData.postValue(PicturesViewState(Fetching, picturesRow))
        lAllPicturesLiveData.postValue(PicturesViewState(Fetched, picturesRow))
    }

    private fun fetching() {
        lPicturesLiveData.postValue(PicturesViewState(Fetching, picturesRowBundle))
    }

    private fun fetched() {
        lPicturesLiveData.postValue(PicturesViewState(Fetched, picturesRowBundle))
    }

    private fun notFetched(message: String) {
        val notFetched = NotFetched.also {
            it.message = message
        }
        lPicturesLiveData.postValue(PicturesViewState(notFetched, picturesRowBundle))
    }

    @Suppress("UNCHECKED_CAST")
    class PictureViewModelFactory(
            private val context: Context,
            private val retrofitService: RetrofitService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PictureViewModel(context, retrofitService) as T
        }
    }
}