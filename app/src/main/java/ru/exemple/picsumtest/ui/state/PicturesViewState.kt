package ru.exemple.picsumtest.ui.state

import ru.exemple.picsumtest.ui.row.PictureRow

data class PicturesViewState(
        val fetchStatus: FetchStatus,
        val pictures: List<PictureRow>
)