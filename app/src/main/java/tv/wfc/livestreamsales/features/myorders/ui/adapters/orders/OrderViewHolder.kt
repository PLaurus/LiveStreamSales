package tv.wfc.livestreamsales.features.myorders.ui.adapters.orders

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderRecipient
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.databinding.ListItemMyOrdersOrderBinding
import tv.wfc.livestreamsales.features.myorders.ui.adapters.orderedproducts.OrderedProductsAdapter
import java.util.concurrent.TimeUnit

class OrderViewHolder(
    orderView: View,
    private val orderedProductDiffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>,
    private val imageLoader: ImageLoader,
    private val holderOwnerDisposables: CompositeDisposable,
    private val computationScheduler: Scheduler,
    private val mainThreadScheduler: Scheduler,
    private val applicationErrorsLogger: IApplicationErrorsLogger,
    private val onCompleteTheOrderButtonClicked: ((orderId: Long) -> Unit)? = null,
    private val onShowMoreInformationAboutOrderButtonClicked: ((orderId: Long) -> Unit)? = null
): RecyclerView.ViewHolder(orderView) {
    private val viewBinding = ListItemMyOrdersOrderBinding.bind(orderView)
    private val context: Context = orderView.context

    private var editOrderButtonDisposable: Disposable? = null
    private var showMoreInformationAboutOrderButtonDisposable: Disposable? = null

    fun bind(order: Order){
        clear()
        initializeOrderNumberText(order)
        initializeCreationDateText(order)
        initializeDeliveryDateText(order)
        initializeDeliveryDateTitleText(order)
        initializeOrderStatusText(order)
        initializeOrderedProductsRecyclerView(order)
        initializeShortOrderInformationTitleText(order)
        initializeShortInformationDeliveryAddressText(order)
        initializeShortInformationDateText(order)
        initializeShortInformationRecipientText(order)
        initializeSumText(order)
        initializeCompleteTheOrderButton(order)
        initializeShowMoreInformationAboutOrderButton(order)
    }

    fun clear(){
        clearOrderNumberText()
        clearCreationDateText()
        clearDeliveryDateText()
        clearDeliveryDateTitleText()
        clearOrderStatusText()
        clearOrderedProductsRecyclerView()
        clearShortOrderInformationTitleText()
        clearShortInformationDeliveryAddressText()
        clearShortInformationDateText()
        clearShortInformationRecipientText()
        clearSumText()
        clearCompleteTheOrderButton()
        clearShowMoreInformationAboutOrderButton()
    }

    private fun initializeOrderNumberText(order: Order){
        viewBinding.orderNumberText.text = context.getString(
            R.string.fragment_my_orders_order_number_text,
            order.id.toString()
        )
    }

    private fun clearOrderNumberText(){
        viewBinding.orderNumberText.text = ""
    }

    private fun initializeCreationDateText(order: Order){
        viewBinding.creationDateText.text = getFormattedDate(order.orderDate)
    }

    private fun clearCreationDateText(){
        viewBinding.creationDateText.text = ""
    }

    private fun initializeDeliveryDateText(order: Order){
        val deliveryDate = order.deliveryDate

        viewBinding.deliveryDateText.run{
            if(deliveryDate != null){
                visibility = View.VISIBLE
                text = getFormattedDate(deliveryDate)
            } else {
                visibility = View.GONE
                text = ""
            }
        }
    }

    private fun clearDeliveryDateText(){
        viewBinding.deliveryDateText.run{
            text = ""
            visibility = View.GONE
        }
    }

    private fun initializeDeliveryDateTitleText(order: Order){
        val deliveryDate = order.deliveryDate

        viewBinding.deliveryDateTitleText.run{
            visibility = if(deliveryDate != null) View.VISIBLE else View.GONE
        }
    }

    private fun clearDeliveryDateTitleText(){
        viewBinding.deliveryDateTitleText.visibility = View.GONE
    }

    private fun initializeOrderStatusText(order: Order){
        viewBinding.orderStatusText.run{
            when(order.status){
                Order.Status.JUST_MADE -> {
                    text = context.getString(R.string.fragment_my_orders_order_status_created)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.myOrders_orderStatus_created))
                }
                Order.Status.IN_PROGRESS -> {
                    text = context.getString(R.string.fragment_my_orders_order_status_in_progress)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.myOrders_orderStatus_in_progress))
                }
                Order.Status.DONE -> {
                    text = context.getString(R.string.fragment_my_orders_order_status_done)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.myOrders_orderStatus_done))
                }
            }
        }
    }

    private fun clearOrderStatusText(){
        viewBinding.orderStatusText.run{
            text = ""
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorTransparent))
        }
    }

    private fun initializeOrderedProductsRecyclerView(order: Order){
        viewBinding.orderedProductsRecyclerView.run{
            adapter = OrderedProductsAdapter(
                orderedProductDiffUtilItemCallback,
                imageLoader
            )

            overScrollMode = View.OVER_SCROLL_NEVER

            val orderedProducts = order.products
            visibility = if(orderedProducts.isNotEmpty()) View.VISIBLE else View.GONE
            (adapter as OrderedProductsAdapter).submitList(orderedProducts)
        }
    }

    private fun clearOrderedProductsRecyclerView(){
        viewBinding.orderedProductsRecyclerView.adapter = null
    }

    private fun initializeShortOrderInformationTitleText(order: Order){
        viewBinding.shortOrderInformationTitleText.visibility =
            if(order.deliveryAddress == null
                && order.deliveryDate == null
                && order.orderRecipient == null
            ) View.GONE else View.VISIBLE
    }

    private fun clearShortOrderInformationTitleText(){
        viewBinding.shortOrderInformationTitleText.visibility = View.GONE
    }

    private fun initializeShortInformationDeliveryAddressText(order: Order){
        val deliveryAddress = order.deliveryAddress
        val notEnteredText = context.getString(R.string.fragment_my_orders_short_order_information_delivery_address_not_entered)

        viewBinding.shortInformationDeliveryAddressText.run{
            if(deliveryAddress != null){
                visibility = View.VISIBLE

                text = context.getString(
                    R.string.fragment_my_orders_short_order_information_delivery_address,
                    deliveryAddress.city,
                    deliveryAddress.street,
                    deliveryAddress.building,
                    deliveryAddress.flat,
                    deliveryAddress.floor ?: notEnteredText
                )
            } else{
                visibility = View.GONE
                text = notEnteredText
            }
        }
    }

    private fun clearShortInformationDeliveryAddressText(){
        viewBinding.shortInformationDeliveryAddressText.run{
            visibility = View.GONE
            text = ""
        }
    }

    private fun initializeShortInformationRecipientText(order: Order){
        val orderRecipient = order.orderRecipient

        viewBinding.shortInformationRecipientText.run{
            if(orderRecipient != null){
                visibility = View.VISIBLE
                text = orderRecipient.toStringForShortOrderRecipientText()
            } else{
                visibility = View.GONE
                text = ""
            }
        }
    }

    private fun OrderRecipient.toStringForShortOrderRecipientText(): String{
        val result = StringBuilder()
        result.append(this.name)

        surname?.let {
            result.append(" ")
            result.append(it)
        }

        email?.let {
            result.append(", ")
            result.append(it)
        }

        return result.toString()
    }

    private fun clearShortInformationRecipientText(){
        viewBinding.shortInformationRecipientText.run{
            visibility = View.GONE
            text = ""
        }
    }

    private fun initializeShortInformationDateText(order: Order){
        val deliveryDate = order.deliveryDate

        viewBinding.shortInformationDateText.run{
            if(deliveryDate != null){
                visibility = View.VISIBLE
                text = getFormattedDate(deliveryDate)
            } else{
                visibility = View.GONE
                text = ""
            }
        }
    }

    private fun clearShortInformationDateText(){
        viewBinding.shortInformationDateText.run {
            visibility = View.GONE
            text = ""
        }
    }

    private fun initializeSumText(order: Order){
        viewBinding.sumText.run{
            text = context.getString(R.string.fragment_my_orders_sum_text, order.orderPrice)
        }
    }

    private fun clearSumText(){
        viewBinding.sumText.text = ""
    }

    private fun initializeCompleteTheOrderButton(order: Order){
        viewBinding.completeTheOrderButton.run {
            visibility = when(order.status){
                Order.Status.JUST_MADE -> View.VISIBLE
                else -> View.GONE
            }

            val orderId = order.id

            editOrderButtonDisposable?.dispose()
            editOrderButtonDisposable = clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError(applicationErrorsLogger::logError)
                .subscribeBy(
                    onNext = { onCompleteTheOrderButtonClicked?.invoke(orderId) }
                )
                .addTo(holderOwnerDisposables)
        }
    }

    private fun clearCompleteTheOrderButton(){
        viewBinding.completeTheOrderButton.run {
            editOrderButtonDisposable?.dispose()
            visibility = View.GONE
        }
    }

    private fun initializeShowMoreInformationAboutOrderButton(order: Order){
        viewBinding.showMoreInformationAboutOrderButton.run {
            visibility = when(order.status){
                Order.Status.JUST_MADE -> View.GONE
                else -> View.VISIBLE
            }

            val orderId = order.id

            showMoreInformationAboutOrderButtonDisposable?.dispose()
            showMoreInformationAboutOrderButtonDisposable = clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .doOnError(applicationErrorsLogger::logError)
                .subscribeBy(
                    onNext = { onShowMoreInformationAboutOrderButtonClicked?.invoke(orderId) }
                )
                .addTo(holderOwnerDisposables)
        }
    }

    private fun clearShowMoreInformationAboutOrderButton(){
        viewBinding.showMoreInformationAboutOrderButton.run {
            showMoreInformationAboutOrderButtonDisposable?.dispose()
            visibility = View.GONE
        }
    }

    private fun getFormattedDate(dateTime: DateTime): String{
        val currentTimeZone = DateTimeZone.getDefault()
        return dateTime.withZone(currentTimeZone).toString("dd.MM.yyyy")
    }
}