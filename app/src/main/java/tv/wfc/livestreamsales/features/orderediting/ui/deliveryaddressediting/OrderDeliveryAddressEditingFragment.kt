package tv.wfc.livestreamsales.features.orderediting.ui.deliveryaddressediting

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseFragment
import tv.wfc.livestreamsales.databinding.FragmentOrderDeliveryAddressEditingBinding
import tv.wfc.livestreamsales.features.orderediting.di.OrderDeliveryAddressEditingComponent
import tv.wfc.livestreamsales.features.orderediting.viewmodel.IOrderEditingViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OrderDeliveryAddressEditingFragment: BaseFragment(R.layout.fragment_order_delivery_address_editing){
    private val navigationController by lazy { findNavController() }

    private var viewBinding: FragmentOrderDeliveryAddressEditingBinding? = null

    private lateinit var orderDeliveryAddressEditingComponent: OrderDeliveryAddressEditingComponent

    @Inject
    lateinit var viewModel: IOrderEditingViewModel

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
    }

    override fun onDestroyView() {
        unbindView()
        super.onDestroyView()
    }

    private fun createOrderEditingComponent(){
        if(::orderDeliveryAddressEditingComponent.isInitialized) return

        val viewModelStoreOwner = navigationController.previousBackStackEntry ?: throw NullPointerException()

        orderDeliveryAddressEditingComponent = appComponent
            .orderDeliveryAddressEditingComponent()
            .create(
                orderDeliveryAddressEditingViewModelStoreOwner = this,
                orderEditingViewModelStoreOwner = viewModelStoreOwner
            )
    }

    private fun injectDependencies(){
        if(!::orderDeliveryAddressEditingComponent.isInitialized) return
        orderDeliveryAddressEditingComponent.inject(this)
    }

    private fun bindView(view: View){
        viewBinding = FragmentOrderDeliveryAddressEditingBinding.bind(view)
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

    private fun onDataIsPrepared() {
        initializeCityEditText()
        initializeCityLayout()
        initializeStreetEditText()
        initializeStreetLayout()
        initializeBuildingEditText()
        initializeBuildingLayout()
        initializeFlatEditText()
        initializeFlatLayout()
        initializeFloorEditText()
        initializeSaveAddressButton()
    }

    private fun initializeCityEditText(){
        viewBinding?.cityEditText?.run {
            viewModel.deliveryCity.observe(viewLifecycleOwner, Observer { city ->
                if(text.toString() == city) return@Observer

                setText(city, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateDeliveryCity(editable.toString())
            }
        }
    }

    private fun initializeCityLayout(){
        viewModel.deliveryCityError.observe(viewLifecycleOwner, { cityError ->
            val errorMessage = when(cityError){
                IOrderEditingViewModel.CityError.FieldIsRequired -> getString(R.string.fragment_order_delivery_address_editing_field_is_required)
                IOrderEditingViewModel.CityError.FieldContainsIllegalSymbols -> getString(R.string.fragment_order_delivery_address_editing_field_contains_illegal_symbols)
                else -> null
            }

            viewBinding?.cityLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeStreetEditText(){
        viewBinding?.streetEditText?.run {
            viewModel.deliveryStreet.observe(viewLifecycleOwner, Observer{ street ->
                if(text.toString() == street) return@Observer

                setText(street, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateDeliveryStreet(editable.toString())
            }
        }
    }

    private fun initializeStreetLayout(){
        viewModel.deliveryStreetError.observe(viewLifecycleOwner, { streetError ->
            val errorMessage = when(streetError){
                IOrderEditingViewModel.StreetError.FieldIsRequired -> getString(R.string.fragment_order_delivery_address_editing_field_is_required)
                IOrderEditingViewModel.StreetError.FieldContainsIllegalSymbols -> getString(R.string.fragment_order_delivery_address_editing_field_contains_illegal_symbols)
                else -> null
            }

            viewBinding?.streetLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeBuildingEditText(){
        viewBinding?.buildingEditText?.run {
            viewModel.deliveryBuilding.observe(viewLifecycleOwner, Observer{ building ->
                if(text.toString() == building) return@Observer

                setText(building, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateDeliveryBuilding(editable.toString())
            }
        }
    }

    private fun initializeBuildingLayout(){
        viewModel.deliveryBuildingError.observe(viewLifecycleOwner, { buildingError ->
            val errorMessage = when(buildingError){
                IOrderEditingViewModel.BuildingError.FieldIsRequired -> getString(R.string.fragment_order_delivery_address_editing_field_is_required)
                IOrderEditingViewModel.BuildingError.FieldContainsIllegalSymbols -> getString(R.string.fragment_order_delivery_address_editing_field_contains_illegal_symbols)
                else -> null
            }

            viewBinding?.buildingLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeFlatEditText(){
        viewBinding?.flatEditText?.run {
            viewModel.deliveryFlat.observe(viewLifecycleOwner, Observer { flat ->
                if(text.toString() == flat) return@Observer

                setText(flat, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateDeliveryFlat(editable.toString())
            }
        }
    }

    private fun initializeFlatLayout(){
        viewModel.deliveryFlatError.observe(viewLifecycleOwner, { flatError ->
            val errorMessage = when(flatError){
                IOrderEditingViewModel.FlatError.FieldContainsIllegalSymbols -> getString(R.string.fragment_order_delivery_address_editing_field_contains_illegal_symbols)
                else -> null
            }

            viewBinding?.flatLayout?.run{
                isErrorEnabled = errorMessage != null
                error = errorMessage
            }
        })
    }

    private fun initializeFloorEditText(){
        viewBinding?.floorEditText?.run {
            viewModel.deliveryFloor.observe(viewLifecycleOwner, Observer{ floor: Int? ->
                val floorText = floor?.toString()
                if(text.toString() == floorText) return@Observer

                setText(floorText, TextView.BufferType.EDITABLE)
            })

            addTextChangedListener { editable ->
                viewModel.updateDeliveryFloor(editable.toString().toIntOrNull())
            }
        }
    }

    private fun initializeSaveAddressButton(){
        viewBinding?.saveAddressButton?.run{
            viewModel.deliveryAddress.observe(viewLifecycleOwner){ address ->
                isEnabled = address != null
            }

            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { navigationController.navigateUp() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }
}