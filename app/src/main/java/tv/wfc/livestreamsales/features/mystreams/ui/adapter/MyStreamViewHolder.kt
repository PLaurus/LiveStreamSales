package tv.wfc.livestreamsales.features.mystreams.ui.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.databinding.ListItemMyStreamBinding

class MyStreamViewHolder(
    view: View,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(view) {
    private val viewBinding = ListItemMyStreamBinding.bind(view)

    private val context = viewBinding.root.context

    private var previewImageLoaderDisposable: Disposable? = null

    fun bind(myStream: MyStream) {
        clear()
        initializePreviewImage(myStream)
        initializeNameTextView(myStream)
        initializeDescriptionTextView(myStream)
        initializeStartDateTextView(myStream)
        initializeStartTimeTextView(myStream)
    }

    fun clear() {
        clearPreviewImage()
        clearNameTextView()
        clearDescriptionTextView()
        clearStartDateTextView()
        clearStartTimeTextView()
    }

    private fun initializePreviewImage(myStream: MyStream) {
        myStream.imageUrl?.let { imageUrl ->
            val imageRequest = ImageRequest.Builder(context)
                .data(imageUrl)
                .target(
                    onError = { clearPreviewImage() },
                    onSuccess = ::setPreviewImageDrawable
                )
                .build()

            previewImageLoaderDisposable?.dispose()
            previewImageLoaderDisposable = imageLoader.enqueue(imageRequest)
        }
    }

    private fun setPreviewImageDrawable(drawable: Drawable) {
        viewBinding.previewImage.setImageDrawable(drawable)
    }

    private fun clearPreviewImage() {
        previewImageLoaderDisposable?.dispose()
        viewBinding.previewImage.setImageDrawable(null)
    }

    private fun initializeNameTextView(myStream: MyStream) {
        viewBinding.nameTextView.text = myStream.title
    }

    private fun clearNameTextView() {
        viewBinding.nameTextView.text = ""
    }

    private fun initializeDescriptionTextView(myStream: MyStream) {
        viewBinding.descriptionTextView.text = myStream.description
    }

    private fun clearDescriptionTextView() {
        viewBinding.descriptionTextView.text = ""
    }

    private fun initializeStartDateTextView(myStream: MyStream) {
        val streamStartDate = myStream.startsAt

        viewBinding.startDateTextView.text = getFormattedDate(streamStartDate)
    }

    private fun clearStartDateTextView() {
        viewBinding.startDateTextView.text = ""
    }

    private fun initializeStartTimeTextView(myStream: MyStream) {
        val streamStartTime = myStream.startsAt

        viewBinding.startTimeTextView.text = getFormattedTime(streamStartTime)
    }

    private fun clearStartTimeTextView() {
        viewBinding.startTimeTextView.text = ""
    }

    private fun getFormattedDate(date: DateTime): String {
        val currentTimeZone = DateTimeZone.getDefault()
        return date.withZone(currentTimeZone).toString("dd.MM.YYYY")
    }

    private fun getFormattedTime(time: DateTime): String {
        val currentTimeZone = DateTimeZone.getDefault()
        return time.withZone(currentTimeZone).toString("HH:mm")
    }
}