package ru.exemple.picsumtest.ui.state

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    class NotFetched(var message: String) : FetchStatus()
    object InvalidPassword : FetchStatus()
}