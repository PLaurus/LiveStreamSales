package tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.stream.PublicStream
import tv.wfc.livestreamsales.databinding.ListItemLiveBroadcastCardBinding

class LiveBroadcastViewHolder(
    liveBroadcastPage: View,
    private val imageLoader: ImageLoader,
    private val onItemClick: (broadcastId: Long) -> Unit
): RecyclerView.ViewHolder(liveBroadcastPage){
    private val viewBinding = ListItemLiveBroadcastCardBinding.bind(liveBroadcastPage)
    private val context: Context
        get() = viewBinding.root.context

    private var liveBroadcastImageLoaderDisposable: Disposable? = null

    fun bind(stream: PublicStream){
        clearView()

        bindLiveBroadcastId(stream.id)
        bindLiveBroadcastImage(stream.imageUrl)
        initializeLiveBroadcastTitleText(stream)
    }

    private fun clearView(){
        clearItemClickListener()
        clearLiveBroadcastImage()
        clearLiveBroadcastTitleText()
    }

    private fun clearItemClickListener(){
        viewBinding.root.setOnClickListener(null)
    }

    private fun clearLiveBroadcastImage(){
        liveBroadcastImageLoaderDisposable?.dispose()
        viewBinding.image.setImageDrawable(null)
    }

    private fun bindLiveBroadcastId(broadcastId: Long){
        viewBinding.root.setOnClickListener {
            onItemClick(broadcastId)
        }
    }

    private fun bindLiveBroadcastImage(uri: String?){
        val context = viewBinding.image.context

        if(uri != null){
            val imageRequest = ImageRequest.Builder(context)
                .data(uri)
                .target(
                    onError = { setDefaultLiveBroadcastImage() },
                    onSuccess = ::setLiveBroadcastImageDrawable
                )
                .build()

            liveBroadcastImageLoaderDisposable = imageLoader.enqueue(imageRequest)
        } else{
            setDefaultLiveBroadcastImage()
        }
    }

    private fun initializeLiveBroadcastTitleText(stream: PublicStream){
        viewBinding.liveBroadcastsTitle.text = stream.title
    }

    private fun clearLiveBroadcastTitleText(){
        viewBinding.liveBroadcastsTitle.text = ""
    }

    private fun setLiveBroadcastImageDrawable(drawable: Drawable){
        viewBinding.image.apply{
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageDrawable(drawable)
        }
    }

    private fun setDefaultLiveBroadcastImage(){
        viewBinding.image.apply{
            scaleType = ImageView.ScaleType.FIT_CENTER
            val defaultDrawable = createDefaultLiveBroadcastImage()
            setImageDrawable(defaultDrawable)
        }
    }

    private fun createDefaultLiveBroadcastImage(): Drawable? {
        return context.getDrawableCompat(
            R.drawable.ic_baseline_live_tv_24,
            R.color.liveBroadcastItem_image_placeholderTint
        )
    }
}