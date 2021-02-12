package ru.exemple.picsumtest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.exemple.picsumtest.R
import ru.exemple.picsumtest.ui.row.PictureRow

class PicturesAdapter(private val pictures: MutableList<PictureRow>) :
        RecyclerView.Adapter<BaseVH>() {

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    private var isLoaderVisible = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                PicturesAdapterViewHolderNormal(layoutInflater.inflate(R.layout.adapter_picture_item_pic, parent, false))
            }
            else -> PictureAdapterViewHolderLoading(layoutInflater.inflate(R.layout.adapter_picture_item_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible && (position == pictures.size - 1)) {
            VIEW_TYPE_LOADING
        } else VIEW_TYPE_NORMAL
    }

    fun notifyDataChange(pictures: List<PictureRow>) {
        this.pictures.clear()
        this.pictures.addAll(pictures)
        notifyDataSetChanged()
    }

    fun addNextPagePictures(pictures: List<PictureRow>) {
        this.pictures.addAll(pictures)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        pictures.add(PictureRow("", ""))
        notifyItemInserted(pictures.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = pictures.size - 1
        if (position == -1) return
        val item: PictureRow? = getItem(position)
        if (item != null) {
            pictures.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): PictureRow? {
        return pictures[position]
    }

    inner class PicturesAdapterViewHolderNormal(itemView: View) : BaseVH(itemView) {
        private val ivPicture: ImageView =
                itemView.findViewById(R.id.adapter_picture_item_pic__ivPicture)
        private val tvAuthor: TextView = itemView.findViewById(R.id.adapter_picture_item_pic__tvAuthor)
        override fun onBind(position: Int) {
            tvAuthor.text = pictures[position].author
            Picasso.get()
                    .load(pictures[position].url)
                    .fit()
                    .centerInside()
                    .into(ivPicture)
        }
    }

    inner class PictureAdapterViewHolderLoading(itemView: View) : BaseVH(itemView)
}