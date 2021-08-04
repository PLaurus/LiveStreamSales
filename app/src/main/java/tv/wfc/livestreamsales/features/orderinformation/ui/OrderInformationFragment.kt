package tv.wfc.livestreamsales.features.orderinformation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.laurus.p.tools.floatKtx.format
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentOrderInformationBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.orderinformation.di.OrderInformationComponent
import tv.wfc.livestreamsales.features.orderinformation.ui.adapters.oderedproducts.OrderedProductsAdapter
import tv.wfc.livestreamsales.features.orderinformation.viewmodel.IOrderInformationViewModel
import javax.inject.Inject

class OrderInformationFragment: BaseFragment(R.layout.fragment_order_information) {
    private val navigationController by lazy { findNavController() }
    private val navigationArguments by navArgs<OrderInformationFragmentArgs>()

    private var viewBinding: FragmentOrderInformationBinding? = null

    private lateinit var orderInformationComponent: OrderInformationComponent

    @Inject
    lateinit var viewModel: IOrderInformationViewModel

    @Inject
    lateinit var orderedProductsDiffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createOrderInformationComponent()
        injectDependencies()
        prepareViewModel(navigationArguments.orderId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createOrderInformationComponent(){
        if(::orderInformationComponent.isInitialized) return

        orderInformationComponent = appComponent
            .orderInformationComponent()
            .create(this)
    }

    private fun injectDependencies(){
        if(!::orderInformationComponent.isInitialized) return
        orderInformationComponent.inject(this)
    }

    private fun prepareViewModel(orderId: Long){
        viewModel.prepareData(orderId)
    }

    private fun bindView(view: View){
        viewBinding = FragmentOrderInformationBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            addOnDataIsPreparedListener(::onDataIsPrepared)

            attachViewModel(viewLifecycleOwner, viewModel)

            viewModel.isAnyOperationInProgress.observe(viewLifecycleOwner){ isAnyOperationInProgress ->
                if(isAnyOperationInProgress){
                    showOperationProgress()
                } else {
                    hideOperationProgress()
                }
            }
        }
    }

    private fun manageNavigation(){
        viewModel.nextDestinationEvent.observe(viewLifecycleOwner){ nextDestination ->
            when(nextDestination){
                IOrderInformationViewModel.NextDestination.Close -> {
                    navigationController.navigateUp()
                }
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeToolbar()
        initializeOrderStatusText()
        initializeCreationDateText()
        initializeDeliveryDateTitleText()
        initializeDeliveryDateText()
        initializeDeliveryAddressTitleText()
        initializeDeliveryAddressText()
        initializeFullDeliveryDateTitleText()
        initializeFullDeliveryDateText()
        initializeOrderRecipientTitleText()
        initializeOrderRecipientText()
        initializeSumText()
        initializeOrderedProductsTitleText()
        initializeOrderedProductsRecyclerView()
    }

    private fun initializeToolbar(){
        viewModel.orderId.observe(viewLifecycleOwner){ orderId ->
            val title = getString(R.string.fragment_order_information_order_number_toolbar_title, orderId.toString())
            (requireActivity() as MainAppContentActivity).setToolbarTitle(title)
        }
    }

    private fun initializeOrderStatusText(){
        viewBinding?.orderStatusText?.run{
            viewModel.orderStatus.observe(viewLifecycleOwner){ orderStatus ->
                when(orderStatus){
                    Order.Status.CREATED -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_information_order_status_created)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderInformation_orderStatus_notPaid)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.PAID -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_information_order_status_paid)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderInformation_orderStatus_paid)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.WAITING -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_information_order_status_waiting)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderInformation_orderStatus_inProgress)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.DONE -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_information_order_status_done)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderInformation_orderStatus_done)
                        setBackgroundColor(backgroundColor)
                    }
                    else -> {
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initializeCreationDateText(){
        viewBinding?.creationDateText?.run{
            viewModel.orderCreationDate.observe(viewLifecycleOwner){ orderCreationDate ->
                text = orderCreationDate.toString("dd MMMM")
            }
        }
    }

    private fun initializeDeliveryDateTitleText(){
        viewBinding?.deliveryDateTitleText?.run{
            viewModel.orderDeliveryDate.observe(viewLifecycleOwner){ orderDeliveryDate ->
                visibility = if(orderDeliveryDate != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializeDeliveryDateText(){
        viewBinding?.deliveryDateText?.run{
            viewModel.orderDeliveryDate.observe(viewLifecycleOwner){ orderDeliveryDate ->
                if(orderDeliveryDate == null){
                    visibility = View.GONE
                    text = ""
                } else{
                    text = orderDeliveryDate.toString("dd MMMM")
                    visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initializeDeliveryAddressTitleText(){
        viewBinding?.deliveryAddressTitleText?.run{
            viewModel.deliveryAddress.observe(viewLifecycleOwner){ deliveryAddress ->
                visibility = if(deliveryAddress != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializeDeliveryAddressText(){
        viewBinding?.deliveryAddressText?.run {
            val notEnteredText = context.getString(R.string.fragment_order_information_delivery_address_not_entered)

            viewModel.deliveryAddress.observe(viewLifecycleOwner){ deliveryAddress ->
                if(deliveryAddress != null){
                    text = context.getString(
                        R.string.fragment_my_orders_short_order_information_delivery_address,
                        deliveryAddress.city,
                        deliveryAddress.street,
                        deliveryAddress.building,
                        deliveryAddress.flat,
                        deliveryAddress.floor ?: notEnteredText
                    )

                    visibility = View.VISIBLE
                } else{
                    visibility = View.GONE
                    text = notEnteredText
                }
            }
        }
    }

    private fun initializeFullDeliveryDateTitleText(){
        viewBinding?.fullDeliveryDateTitleText?.run{
            viewModel.orderDeliveryDate.observe(viewLifecycleOwner){ orderDeliveryDate ->
                visibility = if(orderDeliveryDate != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializeFullDeliveryDateText(){
        viewBinding?.fullDeliveryDateText?.run{
            viewModel.orderDeliveryDate.observe(viewLifecycleOwner){ orderDeliveryDate ->
                if(orderDeliveryDate != null){
                    text = orderDeliveryDate.toString("dd MMMM yyyy - HH:mm")
                    visibility = View.VISIBLE
                }
                else{
                    visibility = View.GONE
                    text = ""
                }
            }
        }
    }

    private fun initializeOrderRecipientTitleText(){
        viewBinding?.orderRecipientTitleText?.run{
            viewModel.orderRecipient.observe(viewLifecycleOwner){ orderRecipient ->
                visibility = if(orderRecipient != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializeOrderRecipientText(){
        viewBinding?.orderRecipientText?.run{
            viewModel.orderRecipient.observe(viewLifecycleOwner){ orderRecipient ->
                if(orderRecipient != null){
                    val orderRecipientStringBuilder = StringBuilder()
                    orderRecipientStringBuilder.append(orderRecipient.name)

                    orderRecipient.surname?.let {
                        orderRecipientStringBuilder.append(" ")
                        orderRecipientStringBuilder.append(it)
                    }

                    orderRecipient.email?.let {
                        orderRecipientStringBuilder.append(", ")
                        orderRecipientStringBuilder.append(it)
                    }

                    text = orderRecipientStringBuilder.toString()
                    visibility = View.VISIBLE
                } else{
                    text = ""
                    visibility = View.GONE
                }
            }
        }
    }

    private fun initializeSumText(){
        viewBinding?.sumText?.run{
            viewModel.orderSum.observe(viewLifecycleOwner){ orderSum ->
                val formattedSum = orderSum.format()
                text = getString(R.string.fragment_order_information_sum_text, formattedSum)
            }
        }
    }

    private fun initializeOrderedProductsTitleText(){
        viewBinding?.orderedProductsTitleText?.run{
            viewModel.orderedProducts.observe(viewLifecycleOwner){ orderedProducts ->
                visibility = if(orderedProducts.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    private fun initializeOrderedProductsRecyclerView(){
        viewBinding?.orderedProductsRecyclerView?.run{
            adapter = OrderedProductsAdapter(
                orderedProductsDiffUtilItemCallback,
                imageLoader
            )

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            viewModel.orderedProducts.observe(viewLifecycleOwner){ orderedProducts ->
                (adapter as OrderedProductsAdapter).submitList(orderedProducts)
            }
        }
    }
}