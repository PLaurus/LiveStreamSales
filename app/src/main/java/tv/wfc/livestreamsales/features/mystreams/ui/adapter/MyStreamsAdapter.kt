package tv.wfc.livestreamsales.features.mystreams.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.stream.MyStream

class MyStreamsAdapter(
    myStreamsDiffUtilItemCallback: DiffUtil.ItemCallback<MyStream>,
    private val imageLoader: ImageLoader
) : ListAdapter<MyStream, MyStreamViewHolder>(myStreamsDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStreamViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_my_stream, parent, false)

        return MyStreamViewHolder(view, imageLoader)
    }

    override fun onBindViewHolder(holder: MyStreamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}