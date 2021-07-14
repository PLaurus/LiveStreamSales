package tv.wfc.livestreamsales.features.orderediting.ui.orderediting

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.tools.floatKtx.format
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentOrderEditingBinding
import tv.wfc.livestreamsales.features.mainappcontent.ui.MainAppContentActivity
import tv.wfc.livestreamsales.features.orderediting.di.OrderEditingComponent
import tv.wfc.livestreamsales.features.orderediting.ui.orderediting.adapters.oderedproducts.OrderedProductsAdapter
import tv.wfc.livestreamsales.features.orderediting.viewmodel.IOrderEditingViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderEditingFragment: BaseFragment(R.layout.fragment_order_editing){
    private val navigationController by lazy { findNavController() }
    private val navigationArguments by navArgs<OrderEditingFragmentArgs>()

    private var viewBinding: FragmentOrderEditingBinding? = null

    private lateinit var orderEditingComponent: OrderEditingComponent

    @Inject
    lateinit var viewModel: IOrderEditingViewModel

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
        createOrderEditingComponent()
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

    private fun createOrderEditingComponent(){
        if(::orderEditingComponent.isInitialized) return

        val viewModelStoreOwner = navigationController.currentBackStackEntry?: throw NullPointerException()

        orderEditingComponent = appComponent
            .orderEditingComponent()
            .create(viewModelStoreOwner)
    }

    private fun injectDependencies(){
        if(!::orderEditingComponent.isInitialized) return
        orderEditingComponent.inject(this)
    }

    private fun prepareViewModel(orderId: Long){
        viewModel.prepareData(orderId)
    }

    private fun bindView(view: View){
        viewBinding = FragmentOrderEditingBinding.bind(view)
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
                IOrderEditingViewModel.NextDestination.Close -> {
                    navigationController.navigateUp()
                }
                IOrderEditingViewModel.NextDestination.OrderDeliveryAddressEditing -> {
                    navigateToOrderDeliveryAddressEditingDestination()
                }
                IOrderEditingViewModel.NextDestination.DeliveryDatePicker -> {
                    navigateToDeliveryDatePicker()
                }
                IOrderEditingViewModel.NextDestination.DeliveryTimePicker -> {
                    navigateToTimePicker()
                }
                IOrderEditingViewModel.NextDestination.OrderConfirmed ->{
                    navigateToOrderIsConfirmedDestination()
                }
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeToolbar()
        initializeOrderStatusText()
        initializeDeliveryAddressText()
        initializeEditDeliveryAddressButton()
        initializeEditDeliveryDateButton()
        initializeDeliveryDateText()
        initializeOrderRecipientText()
        initializeYourOrderTitleText()
        initializeOrderedProductsRecyclerView()
        initializeSumText()
        initializeConfirmOrderButton()
        initializeSnackBar()
    }

    private fun initializeToolbar(){
        viewModel.orderId.observe(viewLifecycleOwner){ orderId ->
            val title = getString(R.string.fragment_order_editing_order_number_toolbar_title, orderId.toString())
            (requireActivity() as MainAppContentActivity).setToolbarTitle(title)
        }
    }

    private fun initializeOrderStatusText(){
        viewBinding?.orderStatusText?.run{
            viewModel.orderStatus.observe(viewLifecycleOwner){ orderStatus ->
                when(orderStatus){
                    Order.Status.NOT_PAID -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_editing_order_status_not_paid)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderEditing_orderStatus_notPaid)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.PAID -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_editing_order_status_paid)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderEditing_orderStatus_paid)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.IN_PROGRESS -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_editing_order_status_in_progress)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderEditing_orderStatus_inProgress)
                        setBackgroundColor(backgroundColor)
                    }
                    Order.Status.DONE -> {
                        visibility = View.VISIBLE
                        text = getString(R.string.fragment_order_editing_order_status_done)
                        val backgroundColor = ContextCompat.getColor(context, R.color.orderEditing_orderStatus_done)
                        setBackgroundColor(backgroundColor)
                    }
                    else -> {
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initializeDeliveryAddressText(){
        viewBinding?.deliveryAddressText?.run {
            val addressIsNotEnteredText = context.getString(R.string.fragment_order_editing_delivery_address_text_not_entered)
            val floorIsNotEntered = context.getString(R.string.fragment_order_editing_delivery_address_text_floor_not_entered)

            viewModel.deliveryAddress.observe(viewLifecycleOwner){ deliveryAddress ->
                text = if(deliveryAddress != null){
                    context.getString(
                        R.string.fragment_order_editing_delivery_address_text_entered,
                        deliveryAddress.city,
                        deliveryAddress.street,
                        deliveryAddress.building,
                        deliveryAddress.flat,
                        deliveryAddress.floor ?: floorIsNotEntered
                    )
                } else addressIsNotEnteredText
            }
        }
    }

    private fun initializeEditDeliveryAddressButton(){
        viewBinding?.editDeliveryAddressButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.intentToNavigateToOrderDeliveryAddressEditing() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeEditDeliveryDateButton(){
        viewBinding?.editDeliveryDateButton?.run{
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.intentToNavigateToDeliveryDatePicker() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeDeliveryDateText(){
        viewBinding?.deliveryDateText?.run{
            val dateIsNotEnteredText = context.getString(R.string.fragment_order_editing_delivery_date_text_not_entered)

            viewModel.orderDeliveryDateTime.observe(viewLifecycleOwner){ orderDeliveryDateTime ->
                text = if(orderDeliveryDateTime != null){
                    orderDeliveryDateTime.toString("dd MMMM HH:mm")
                } else dateIsNotEnteredText
            }
        }
    }

    private fun initializeOrderRecipientText(){
        viewBinding?.orderRecipientText?.run{
            val orderRecipientIsNotEntered = context.getString(R.string.fragment_order_editing_order_recipient_text_not_entered)

            viewModel.orderRecipient.observe(viewLifecycleOwner){ orderRecipient ->
                text = if(orderRecipient != null){
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

                    orderRecipientStringBuilder.toString()
                } else orderRecipientIsNotEntered
            }
        }
    }

    private fun initializeYourOrderTitleText(){
        viewBinding?.yourOrderTitleText?.run{
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

    private fun initializeSumText(){
        viewBinding?.sumText?.run{
            viewModel.orderSum.observe(viewLifecycleOwner){ orderSum ->
                val formattedSum = orderSum.format()
                text = getString(R.string.fragment_order_editing_sum_text, formattedSum)
            }
        }
    }

    private fun initializeConfirmOrderButton(){
        viewBinding?.confirmOrderButton?.run{
            viewModel.isOrderReadyForConfirmation.observe(viewLifecycleOwner){ isReady ->
                isEnabled = isReady
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.confirmOrder() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeSnackBar(){
        viewBinding?.run {
            viewModel.genericErrorEvent.observe(viewLifecycleOwner){ errorMessage ->
                Snackbar.make(root, errorMessage, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToOrderDeliveryAddressEditingDestination(){
        val action = OrderEditingFragmentDirections.actionToOrderDeliveryAddressEditingDestination()
        navigationController.navigate(action)
    }

    private fun navigateToOrderIsConfirmedDestination(){
        val action = OrderEditingFragmentDirections.actionToOrderIsConfirmedDestination()
        navigationController.navigate(action)
    }

    private fun navigateToDeliveryDatePicker(){
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.fragment_order_editing_delivery_date_title_text)
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )
            .build()

        datePicker.apply{
            addOnPositiveButtonClickListener { selectedDateInMillis ->
                viewModel.updateDeliveryDate(selectedDateInMillis)
                viewModel.intentToNavigateToDeliveryTimePicker()
            }
            show(this@OrderEditingFragment.parentFragmentManager, "deliveryDatePickerDestination")
        }
    }

    private fun navigateToTimePicker(){
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(if(is24HourFormat(requireContext())) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
            .setTitleText(R.string.fragment_order_editing_delivery_time_title_text)
            .build()

        timePicker.apply {
            addOnPositiveButtonClickListener {
                viewModel.updateDeliveryTime(timePicker.hour, timePicker.minute)
            }
            show(this@OrderEditingFragment.parentFragmentManager, "deliveryTimePickerDestination")
        }
    }
}