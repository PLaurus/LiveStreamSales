package tv.wfc.livestreamsales.features.mystreams.ui.adapter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.databinding.ListItemMyStreamBinding
import java.util.concurrent.TimeUnit

class MyStreamViewHolder(
    view: View,
    private val imageLoader: ImageLoader,
    private val computationScheduler: Scheduler,
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger,
    private val disposables: CompositeDisposable,
    private val onClick: (streamId: Long) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val viewBinding = ListItemMyStreamBinding.bind(view)

    private val context = viewBinding.root.context

    private var rootViewClickListenerDisposable: io.reactivex.rxjava3.disposables.Disposable? = null

    private var previewImageLoaderDisposable: Disposable? = null

    fun bind(myStream: MyStream) {
        initializeRootView(myStream)
        initializePreviewImage(myStream)
        initializeNameTextView(myStream)
        initializeDescriptionTextView(myStream)
        initializeStartDateTextView(myStream)
        initializeStartTimeTextView(myStream)
    }

    private fun initializeRootView(myStream: MyStream) {
        clearRootView()

        rootViewClickListenerDisposable = viewBinding.root
            .clicks()
            .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onNext = {
                    val streamId = myStream.id
                    onClick(streamId)
                },
                onError = applicationErrorsLogger::logError
            )
            .addTo(disposables)
    }

    private fun clearRootView() {
        rootViewClickListenerDisposable?.dispose()
    }

    private fun initializePreviewImage(myStream: MyStream) {
        clearPreviewImage()

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
        clearNameTextView()
        viewBinding.nameTextView.text = myStream.title
    }

    private fun clearNameTextView() {
        viewBinding.nameTextView.text = ""
    }

    private fun initializeDescriptionTextView(myStream: MyStream) {
        clearDescriptionTextView()
        viewBinding.descriptionTextView.text = myStream.description
    }

    private fun clearDescriptionTextView() {
        viewBinding.descriptionTextView.text = ""
    }

    private fun initializeStartDateTextView(myStream: MyStream) {
        clearStartDateTextView()

        val streamStartDate = myStream.startsAt

        viewBinding.startDateTextView.text = getFormattedDate(streamStartDate)
    }

    private fun clearStartDateTextView() {
        viewBinding.startDateTextView.text = ""
    }

    private fun initializeStartTimeTextView(myStream: MyStream) {
        clearStartTimeTextView()

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