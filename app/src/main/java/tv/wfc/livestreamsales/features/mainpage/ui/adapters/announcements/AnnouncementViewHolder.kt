package tv.wfc.livestreamsales.features.mainpage.ui.adapters.announcements

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.broadcastinformation.BroadcastBaseInformation
import tv.wfc.livestreamsales.databinding.ItemBroadcastAnnouncementPageBinding

class AnnouncementViewHolder(
    broadcastAnnouncementPage: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(broadcastAnnouncementPage) {
    private val viewBinding = ItemBroadcastAnnouncementPageBinding.bind(broadcastAnnouncementPage)

    private var announcementImageLoaderDisposable: Disposable? = null

    fun bind(broadcastInformation: BroadcastBaseInformation){
        clearView()

        bindAnnouncementImage(broadcastInformation.imageUrl)
        bindAnnouncementDate(broadcastInformation.startsAt)
        bindAnnouncementTime(broadcastInformation.startsAt)
    }

    private fun clearView(){
        clearAnnouncementImage()
        clearAnnouncementDateText()
        clearAnnouncementTimeText()
    }

    private fun clearAnnouncementImage(){
        announcementImageLoaderDisposable?.dispose()
        viewBinding.image.setImageDrawable(null)
    }

    private fun clearAnnouncementDateText(){
        viewBinding.dateText.text = ""
    }

    private fun clearAnnouncementTimeText(){
        viewBinding.timeText.text = ""
    }

    private fun bindAnnouncementImage(uri: String?){
        val context = viewBinding.image.context

        if(uri != null){
            val imageRequest = ImageRequest.Builder(context)
                .data(uri)
                .target(
                    onError = { setDefaultAnnouncementImage() },
                    onSuccess = ::setAnnouncementImageDrawable
                )
                .build()

            announcementImageLoaderDisposable = imageLoader.enqueue(imageRequest)
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

    private fun bindAnnouncementDate(dateTime: DateTime?){
        viewBinding.dateText.text = if(dateTime != null){
            getFormattedDate(dateTime)
        } else ""
    }

    private fun getFormattedDate(dateTime: DateTime): String{
        val currentTimeZone = DateTimeZone.getDefault()
        return dateTime.withZone(currentTimeZone).toString("dd.MM.yyyy")
    }

    private fun bindAnnouncementTime(dateTime: DateTime?) {
        val timeText = viewBinding.timeText
        val context = timeText.context

        timeText.text = if(dateTime != null){
            getFormattedTime(context, dateTime)
        } else ""
    }

    private fun getFormattedTime(context: Context, dateTime: DateTime): String{
        val currentTimeZone = DateTimeZone.getDefault()
        val time = dateTime.withZone(currentTimeZone).toString("HH:mm")
        return context.resources.getString(R.string.item_live_broadcast_page_time, time)
    }
}