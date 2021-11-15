package tv.wfc.livestreamsales.features.mystreams.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.stream.MyStream
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger

class MyStreamsAdapter(
    myStreamsDiffUtilItemCallback: DiffUtil.ItemCallback<MyStream>,
    private val imageLoader: ImageLoader,
    private val computationScheduler: Scheduler,
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger,
    private val disposables: CompositeDisposable,
    private val onClick: (streamId: Long) -> Unit
) : ListAdapter<MyStream, MyStreamViewHolder>(myStreamsDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStreamViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_my_stream, parent, false)

        return MyStreamViewHolder(
            view,
            imageLoader,
            computationScheduler,
            mainThreadScheduler,
            applicationErrorsLogger,
            disposables,
            onClick
        )
    }

    override fun onBindViewHolder(holder: MyStreamViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}