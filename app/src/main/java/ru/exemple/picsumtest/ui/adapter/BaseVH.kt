package ru.exemple.picsumtest.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun onBind(position: Int) {
    }
}