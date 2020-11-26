package ru.exemple.picsumtest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.exemple.picsumtest.App
import ru.exemple.picsumtest.R
import ru.exemple.picsumtest.ui.adapter.PaginationScrollListener
import ru.exemple.picsumtest.ui.adapter.PicturesAdapter
import ru.exemple.picsumtest.ui.row.PictureRow
import ru.exemple.picsumtest.ui.state.FetchStatus.*
import ru.exemple.picsumtest.ui.state.PicturesViewState
import ru.exemple.picsumtest.viewModel.PictureViewModel
import javax.inject.Inject

class PicturesFragment : Fragment() {

    @Inject
    lateinit var pictureViewModelFactory: PictureViewModel.PictureViewModelFactory
    private lateinit var pictureViewModel: PictureViewModel
    private lateinit var picturesAdapter: PicturesAdapter

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var pbFirstLoading: ProgressBar

    private var isLoading = false
    private var isLastPage = false
    private var startPage = 0
    private var totalPage = 4
    private var currentPage = startPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity!!.application as App).getComponent().injectPictureFragment(this)
        pictureViewModel = ViewModelProvider(this, pictureViewModelFactory).get(PictureViewModel::class.java)
        pictureViewModel.picturesLiveData.observe(this) {
            updateDisplayState(it)
        }
        pictureViewModel.getPictures(currentPage + 1)
        picturesAdapter = PicturesAdapter(mutableListOf())
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_pictures, container, false)
        recyclerView = rootView.findViewById(R.id.fragment_picture__rvPictures)
        pbFirstLoading = rootView.findViewById(R.id.fragment_picture__pbFirstLoading)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = picturesAdapter
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                if (currentPage == totalPage) {
                    isLastPage = true
                }
                pictureViewModel.getPictures(currentPage)
            }

            override fun getTotalPageCount(): Int {
                return totalPage
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        return rootView
    }

    private fun updateDisplayState(picturesViewState: PicturesViewState) {
        if (currentPage > 0) {
            when (picturesViewState.fetchStatus) {
                Fetching -> {
                    picturesAdapter.addLoading()
                    Log.d("TAG", "Fetching")
                }
                Fetched -> {
                    isLoading = false
                    picturesAdapter.removeLoading()
                    picturesAdapter.addNextPagePictures(picturesViewState.pictures)
                    Log.d("TAG", "Fetched")
                }
                NotFetched -> {
                    hideLoading()
                    showUnsuccessfulMessage()
                }
            }
        } else {
            when (picturesViewState.fetchStatus) {
                Fetching -> showLoading()
                Fetched -> {
                    hideLoading()
                    updateData(picturesViewState.pictures)
                }
                NotFetched -> {
                    hideLoading()
                    showUnsuccessfulMessage()
                }
            }
        }
    }

    private fun showLoading() {
        pbFirstLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        pbFirstLoading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun updateData(pictures: List<PictureRow>) {
        isLoading = false
        picturesAdapter.notifyDataChange(pictures)
    }

    private fun showUnsuccessfulMessage() {
        Snackbar
                .make(rootView, "Data was fetch unsuccessfully", Snackbar.LENGTH_LONG)
                .show()
    }
}