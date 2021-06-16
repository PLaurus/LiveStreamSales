package tv.wfc.livestreamsales.features.myorders.ui.adapters.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger

class OrdersAdapter(
    ordersDiffUtilItemCallback: DiffUtil.ItemCallback<Order>,
    private val orderedProductsDiffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>,
    private val imageLoader: ImageLoader,
    private val adapterOwnerDisposables: CompositeDisposable,
    private val computationScheduler: Scheduler,
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger,
    private val onCompleteTheOrderButtonClicked: ((orderId: Long) -> Unit)? = null,
    private val onShowMoreInformationAboutOrderButtonClicked: ((orderId: Long) -> Unit)? = null
): ListAdapter<Order, OrderViewHolder>(ordersDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val orderView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_my_orders_order, parent, false)

        return OrderViewHolder(
            orderView,
            orderedProductsDiffUtilItemCallback,
            imageLoader,
            adapterOwnerDisposables,
            computationScheduler,
            mainThreadScheduler,
            applicationErrorsLogger,
            onCompleteTheOrderButtonClicked,
            onShowMoreInformationAboutOrderButtonClicked
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}