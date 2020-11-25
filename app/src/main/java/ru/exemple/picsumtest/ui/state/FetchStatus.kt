package ru.exemple.picsumtest.ui.state

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus() {
        lateinit var message: String
    }

    object InvalidPassword : FetchStatus()
}