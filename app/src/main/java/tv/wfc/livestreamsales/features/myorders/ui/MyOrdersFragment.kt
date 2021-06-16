package tv.wfc.livestreamsales.features.myorders.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import io.reactivex.rxjava3.core.Scheduler
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.orders.Order
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentMyOrdersBinding
import tv.wfc.livestreamsales.features.home.ui.HomeFragment
import tv.wfc.livestreamsales.features.home.ui.HomeFragmentDirections
import tv.wfc.livestreamsales.features.myorders.di.MyOrdersComponent
import tv.wfc.livestreamsales.features.myorders.ui.adapters.orders.OrdersAdapter
import tv.wfc.livestreamsales.features.myorders.viewmodel.IMyOrdersViewModel
import javax.inject.Inject

class MyOrdersFragment: BaseFragment(R.layout.fragment_my_orders){
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentMyOrdersBinding? = null

    private lateinit var myOrdersComponent: MyOrdersComponent

    @Inject
    lateinit var viewModel: IMyOrdersViewModel

    @Inject
    lateinit var ordersDiffUtilItemCallback: DiffUtil.ItemCallback<Order>

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
        createMyOrdersComponent()
        injectDependencies()
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

    private fun createMyOrdersComponent(){
        myOrdersComponent = appComponent
            .myOrdersComponent()
            .create(this)
    }

    private fun injectDependencies(){
        myOrdersComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentMyOrdersBinding.bind(view)
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
                is IMyOrdersViewModel.NextDestination.OrderEditing -> {
                    navigateToOrderEditingDestination(nextDestination.orderId)
                }
                is IMyOrdersViewModel.NextDestination.OrderInformation -> {
                    navigateToOrderInformationDestination(nextDestination.orderId)
                }
                IMyOrdersViewModel.NextDestination.NeedPaymentInformation -> {
                    navigateToNeedPaymentInformation()
                }
                IMyOrdersViewModel.NextDestination.Close -> {
                    navigateToMainPage()
                }
            }
        }
    }

    private fun onDataIsPrepared() {
        initializeOrdersRecyclerView()
        initializeSwipeRefreshLayout()
    }

    private fun initializeSwipeRefreshLayout(){
        viewBinding?.swipeRefreshLayout?.run {
            setOnRefreshListener {
                viewModel.refreshData()
            }

            viewModel.isDataBeingRefreshed.observe(viewLifecycleOwner){ isDataBeingRefreshed ->
                isRefreshing = isDataBeingRefreshed
            }
        }
    }

    private fun initializeOrdersRecyclerView(){
        viewBinding?.ordersRecyclerView?.run{
            adapter = OrdersAdapter(
                ordersDiffUtilItemCallback,
                orderedProductsDiffUtilItemCallback,
                imageLoader,
                viewScopeDisposables,
                computationScheduler,
                mainThreadScheduler,
                applicationErrorsLogger,
                onCompleteTheOrderButtonClicked = viewModel::intentToNavigateToOrderEditing,
                onShowMoreInformationAboutOrderButtonClicked = viewModel::intentToNavigateToOrderInformation
            )

            val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL).apply{
                ContextCompat.getDrawable(context, R.drawable.drawable_list_divider_grey_1dp_vertical)?.let(::setDrawable)
            }
            addItemDecoration(dividerItemDecoration)

            viewModel.orders.observe(viewLifecycleOwner){ orders ->
                (adapter as OrdersAdapter).submitList(orders)
            }
        }
    }

    private fun navigateToOrderEditingDestination(orderId: Long){

    }

    private fun navigateToOrderInformationDestination(orderId: Long){
        val action = HomeFragmentDirections.actionToOrderInformationDestination(orderId)
        navigationController.navigate(action)
    }

    private fun navigateToNeedPaymentInformation(){
        val action = HomeFragmentDirections.actionToNeedPaymentInformationDestination()
        navigationController.navigate(action)
    }

    private fun navigateToMainPage(){
        (parentFragment as HomeFragment).navigateToMainPage()
    }
}