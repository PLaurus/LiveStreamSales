package tv.wfc.livestreamsales.features.mainpage.ui.adapters.livebroadcast

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ItemLiveBroadcastPageBinding
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation

class LiveBroadcastViewHolder(
    liveBroadcastPage: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(liveBroadcastPage){
    private val viewBinding = ItemLiveBroadcastPageBinding.bind(liveBroadcastPage)

    private var liveBroadcastImageLoaderDisposable: Disposable? = null

    fun bind(broadcastInformation: BroadcastBaseInformation){
        clearView()

        bindLiveBroadcastImage(broadcastInformation.imageUrl)
    }

    private fun clearView(){
        clearLiveBroadcastImage()
    }

    private fun clearLiveBroadcastImage(){
        liveBroadcastImageLoaderDisposable?.dispose()
        viewBinding.image.setImageDrawable(null)
    }

    private fun bindLiveBroadcastImage(uri: String?){
        val context = viewBinding.image.context

        if(uri != null){
            val imageRequest = ImageRequest.Builder(context)
                .data(uri)
                .target(
                    onError = { setDefaultAnnouncementImage() },
                    onSuccess = ::setAnnouncementImageDrawable
                )
                .build()

            liveBroadcastImageLoaderDisposable = imageLoader.enqueue(imageRequest)
        } else{
            setDefaultAnnouncementImage()
        }
    }

    private fun setAnnouncementImageDrawable(drawable: Drawable){
        viewBinding.image.apply{
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageDrawable(drawable)
        }
    }

    private fun setDefaultAnnouncementImage(){
        viewBinding.image.apply{
            scaleType = ImageView.ScaleType.FIT_CENTER
            setImageResource(R.drawable.ic_baseline_live_tv_24)
        }
    }
}