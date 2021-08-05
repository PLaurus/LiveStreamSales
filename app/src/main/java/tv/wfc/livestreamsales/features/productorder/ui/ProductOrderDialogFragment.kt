package tv.wfc.livestreamsales.features.productorder.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.Disposable
import com.jakewharton.rxbinding4.view.clicks
import com.laurus.p.recyclerviewitemdecorators.GapBetweenItems
import com.laurus.p.tools.context.getDrawableCompat
import com.laurus.p.tools.floatKtx.format
import com.laurus.p.tools.string.strikeThrough
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.ui.base.BaseDialogFragment
import tv.wfc.livestreamsales.databinding.DialogProductOrderBinding
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductBoxDataDiffUtilItemCallback
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductSpecificationsDiffUtilItemCallback
import tv.wfc.livestreamsales.features.productorder.model.ProductBoxData
import tv.wfc.livestreamsales.features.productorder.model.SelectableSpecification
import tv.wfc.livestreamsales.features.productorder.ui.adapters.cart.ProductsInCartAdapter
import tv.wfc.livestreamsales.features.productorder.ui.adapters.products.ProductBoxesAdapter
import tv.wfc.livestreamsales.features.productorder.ui.adapters.productspecifications.ProductSpecificationsAdapter
import tv.wfc.livestreamsales.features.productorder.ui.adapters.selectablespecifications.SelectableSpecificationsAdapter
import tv.wfc.livestreamsales.features.productorder.viewmodel.IProductOrderViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ProductOrderDialogFragment: BaseDialogFragment(R.layout.dialog_product_order){
    private val navigationController by lazy{ findNavController() }
    private val navigationArguments by navArgs<ProductOrderDialogFragmentArgs>()

    private var viewBinding: DialogProductOrderBinding? = null
    private var oneProductImageLoaderDisposable: Disposable? = null

    private lateinit var productOrderComponent: ProductOrderComponent

    @Inject
    lateinit var viewModel: IProductOrderViewModel

    @Inject
    @MainThreadScheduler
    lateinit var mainThreadScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @Inject
    lateinit var applicationErrorsLogger: IApplicationErrorsLogger

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @ProductSpecificationsDiffUtilItemCallback
    lateinit var productSpecificationsDiffUtilItemCallback: DiffUtil.ItemCallback<Specification<*>>

    @Inject
    @ProductBoxDataDiffUtilItemCallback
    lateinit var productBoxDataDiffUtilItemCallback: DiffUtil.ItemCallback<ProductBoxData>

    @Inject
    lateinit var selectableSpecificationsDiffUtilItemCallback: DiffUtil.ItemCallback<SelectableSpecification<*>>

    @Inject
    lateinit var orderedProductDiffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeProductOrderComponent(context)
        injectDependencies()
        prepareViewModel(navigationArguments.liveBroadcastId)
        dialogHeightAdaptationType = DialogDimensionAdaptationType.MAX_SIZE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initializeContentLoader()
        manageNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
        oneProductImageLoaderDisposable?.dispose()
    }

    private fun initializeProductOrderComponent(context: Context){
        val appComponent = (context.applicationContext as LiveStreamSalesApplication).appComponent

        productOrderComponent = appComponent
            .productOrderComponent()
            .create(this)
    }

    private fun injectDependencies(){
        productOrderComponent.inject(this)
    }

    private fun prepareViewModel(broadcastId: Long){
        viewModel.prepareData(broadcastId)
    }

    private fun bindView(view: View){
        viewBinding = DialogProductOrderBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }

    private fun initializeContentLoader(){
        viewBinding?.contentLoader?.apply {
            clearPreparationListeners()
            attachViewModel(viewLifecycleOwner, viewModel)
            addOnDataIsPreparedListener(::onDataIsPrepared)
            addOnDataPreparationFailureListener(::onDataPreparationFailure)

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
        initializeHeaderLayout()
        initializeOneProductImage()
        initializeOneProductNameText()
        initializeSeveralProductsRecyclerView()
        initializeSeveralProductsDescriptionText()
        initializeProductSpecificationsLayout()
        initializeProductSpecificationsRecyclerView()
        initializeSelectableSpecificationsRecyclerView()
        initializeProductVariantPriceLayout()
        initializeProductVariantPriceText()
        initializeProductVariantOldPriceText()
        initializeAmountSelectionLayout()
        initializeProductVariantAmountText()
        initializeDecreaseProductVariantAmountButton()
        initializeIncreaseProductVariantAmountButton()
        initializeCartRecyclerView()
        initializeBuyButton()
        initializeSumTitleText()
        initializeSumText()
    }

    private fun onDataPreparationFailure() {
        dismissAfterDelay()
    }

    private fun manageNavigation(){
        initializeCloseButton()

        viewModel.areProductsOrderedEvent.observe(viewLifecycleOwner){
            navigateToProductsAreOrderedDialog()
        }
    }

    private fun initializeCloseButton(){
        viewBinding?.closeButton?.apply{
            clicks()
                .throttleFirst(1L, TimeUnit.SECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = {
                        dismiss()
                    },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)
        }
    }

    private fun initializeHeaderLayout(){
        viewModel.productsCount.observe(viewLifecycleOwner, { productsCount ->
            if (productsCount <= 1) {
                hideSeveralProductsHeaderLayout()
                showOneProductHeaderLayout()
            } else {
                hideOneProductHeaderLayout()
                showSeveralProductsHeaderLayout()
            }
        })
    }

    private fun initializeOneProductImage(){
        viewBinding?.oneProductImage?.run {
            scaleType = ImageView.ScaleType.FIT_CENTER

            viewModel.currentProductGroupImageUrl.observe(viewLifecycleOwner, { imageUrl ->
                val placeholder = createDefaultProductDrawable()

                if(imageUrl == null){
                    setImageDrawable(placeholder)
                } else{
                    oneProductImageLoaderDisposable?.dispose()
                    oneProductImageLoaderDisposable = load(imageUrl, imageLoader) {
                        error(placeholder)
                        listener(
                            onError = { _, throwable ->
                                applicationErrorsLogger.logError(throwable)
                            }
                        )
                    }
                }
            })
        }
    }

    private fun initializeOneProductNameText(){
        viewBinding?.oneProductName?.run{
            viewModel.currentProductGroupName.observe(viewLifecycleOwner, { productName ->
                text = productName
            })
        }
    }

    private fun initializeSeveralProductsRecyclerView(){
        viewBinding?.severalProductsRecyclerView?.apply {
            adapter = ProductBoxesAdapter(
                productBoxDataDiffUtilItemCallback,
                imageLoader,
                viewModel::selectProductGroupByPosition
            )

            viewModel.productBoxesData.observe(viewLifecycleOwner, { productBoxesData ->
                (adapter as ProductBoxesAdapter).submitList(productBoxesData)
            })
        }
    }

    private fun initializeSeveralProductsDescriptionText(){
        viewBinding?.severalProductsDescriptionText?.run{
            viewModel.productsCount.observe(viewLifecycleOwner, { productsCount ->
                if (productsCount <= 1) {
                    hideSeveralProductsDescriptionText()
                } else {
                    showSeveralProductsDescriptionText()
                }
            })

            viewModel.currentProductGroupName.observe(viewLifecycleOwner, { productName ->
                text = productName
            })
        }
    }

    private fun initializeProductSpecificationsLayout(){
        viewBinding?.productSpecificationsLayout?.run{
            viewModel.currentProductGroupSpecifications.observe(viewLifecycleOwner, { productSpecifications ->
                if (productSpecifications.isEmpty()) {
                    hideProductSpecificationsLayout()
                } else {
                    showProductSpecificationsLayout()
                }
            })
        }
    }

    private fun initializeProductSpecificationsRecyclerView(){
        viewBinding?.productSpecificationsRecyclerView?.run {
            adapter = ProductSpecificationsAdapter(productSpecificationsDiffUtilItemCallback)
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            viewModel.currentProductGroupSpecifications.observe(viewLifecycleOwner, { productSpecifications ->
                (adapter as ProductSpecificationsAdapter).submitList(productSpecifications)
            })
        }
    }

    private fun initializeSelectableSpecificationsRecyclerView(){
        viewBinding?.selectableSpecificationsRecyclerView?.run {
            addItemDecoration(GapBetweenItems(resources.getDimensionPixelSize(R.dimen.contentMargin)))
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            adapter = SelectableSpecificationsAdapter(selectableSpecificationsDiffUtilItemCallback){ selectableSpecIndex, valueIndex ->
                viewModel.filter(selectableSpecIndex, valueIndex)
            }

            viewModel.currentSelectableSpecifications.observe(viewLifecycleOwner, { selectableSpecifications ->
                (adapter as SelectableSpecificationsAdapter).submitList(selectableSpecifications.toList())
            })
        }
    }

    private fun initializeProductVariantPriceLayout(){
        viewBinding?.productVariantPriceLayout?.apply {
            viewModel.selectedProductPrice.observe(viewLifecycleOwner, { price ->
                if(price != null) {
                    showProductVariantPriceLayout()
                } else{
                    hideProductVariantPriceLayout()
                }
            })
        }
    }

    private fun initializeProductVariantPriceText(){
        viewBinding?.productVariantPriceText?.apply {
            viewModel.selectedProductPrice.observe(viewLifecycleOwner, Observer{ price ->
                text = resources.getString(
                    R.string.dialog_product_order_price,
                    price?.format() ?: return@Observer
                )
            })
        }
    }

    private fun initializeProductVariantOldPriceText(){
        viewBinding?.productVariantOldPriceText?.run {
            viewModel.selectedProductOldPrice.observe(viewLifecycleOwner, { oldPrice ->
                if (oldPrice == null) {
                    hideProductVariantOldPriceText()
                } else {
                    text = getString(
                        R.string.dialog_product_order_price,
                        oldPrice.format()
                    ).strikeThrough()
                    showProductVariantOldPriceText()
                }
            })
        }
    }

    private fun initializeAmountSelectionLayout(){
        viewBinding?.amountSelectionLayout?.apply {
            viewModel.isProductSelected.observe(viewLifecycleOwner, { isProductVariantFiltered ->
                if(isProductVariantFiltered){
                    show()
                } else{
                    hide()
                }
            })
        }
    }

    private fun initializeProductVariantAmountText(){
        viewBinding?.productsVariantAmountText?.apply {
            viewModel.selectedProductAmount.observe(viewLifecycleOwner, { amount ->
                text = amount.toString()
            })
        }
    }

    private fun initializeDecreaseProductVariantAmountButton(){
        viewBinding?.decreaseProductVariantAmountButton?.setOnClickListener {
            viewModel.decreaseSelectedProductAmount()
        }
    }

    private fun initializeIncreaseProductVariantAmountButton(){
        viewBinding?.increaseProductVariantAmountButton?.setOnClickListener {
            viewModel.increaseSelectedProductAmount()
        }
    }

    private fun initializeCartRecyclerView(){
        viewBinding?.cartRecyclerView?.run{
            adapter = ProductsInCartAdapter(
                orderedProductDiffUtilItemCallback,
                imageLoader,
                viewModel::deleteProductFromCart
            ) { /*Select product in cart*/ }

            viewModel.cart.observe(viewLifecycleOwner, { cart ->
                if(cart.isNotEmpty()){
                    show()
                } else hide()

                (adapter as ProductsInCartAdapter).submitList(cart)
            })
        }
    }

    private fun initializeBuyButton(){
        viewBinding?.buyButton?.run {
            clicks()
                .throttleLatest(500L, TimeUnit.MILLISECONDS, computationScheduler)
                .observeOn(mainThreadScheduler)
                .subscribeBy(
                    onNext = { viewModel.orderProducts() },
                    onError = applicationErrorsLogger::logError
                )
                .addTo(viewScopeDisposables)

            viewModel.cart.observe(viewLifecycleOwner){ cart ->
                isEnabled = cart.isNotEmpty()
            }
        }
    }

    private fun initializeSumTitleText(){
        viewBinding?.sumTitleText?.run {
            viewModel.cart.observe(viewLifecycleOwner){ cart ->
                visibility = if(cart.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun initializeSumText(){
        viewBinding?.sumText?.run{
            viewModel.cart.observe(viewLifecycleOwner){ cart ->
                visibility = if(cart.isNotEmpty()) View.VISIBLE else View.GONE
            }

            viewModel.orderedProductsFinalPrice.observe(viewLifecycleOwner, Observer { sum ->
                text = resources.getString(
                    R.string.dialog_product_order_price,
                    sum?.format() ?: return@Observer
                )
            })
        }
    }

    private fun showOneProductHeaderLayout(){
        changeOneProductHeaderLayoutVisibility(toVisible = true)
    }

    private fun hideOneProductHeaderLayout(){
        changeOneProductHeaderLayoutVisibility(toVisible = false)
    }

    private fun changeOneProductHeaderLayoutVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.oneProductHeaderLayout?.visibility = viewVisibility
    }

    private fun showSeveralProductsHeaderLayout(){
        changeSeveralProductsHeaderLayoutVisibility(toVisible = true)
    }

    private fun hideSeveralProductsHeaderLayout(){
        changeSeveralProductsHeaderLayoutVisibility(toVisible = false)
    }

    private fun changeSeveralProductsHeaderLayoutVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.severalProductsHeaderLayout?.visibility = viewVisibility
    }

    private fun showSeveralProductsDescriptionText(){
        changeSeveralProductsDescriptionTextVisibility(toVisible = true)
    }

    private fun hideSeveralProductsDescriptionText(){
        changeSeveralProductsDescriptionTextVisibility(toVisible = false)
    }

    private fun changeSeveralProductsDescriptionTextVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.severalProductsDescriptionText?.visibility = viewVisibility
    }

    private fun showProductSpecificationsLayout(){
        changeProductSpecificationsLayoutVisibility(toVisible = true)
    }

    private fun hideProductSpecificationsLayout(){
        changeProductSpecificationsLayoutVisibility(toVisible = false)
    }

    private fun changeProductSpecificationsLayoutVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.productSpecificationsLayout?.visibility = viewVisibility
    }

    private fun showProductVariantPriceLayout(){
        changeProductVariantPriceLayoutVisibility(toVisible = true)
    }

    private fun hideProductVariantPriceLayout(){
        changeProductVariantPriceLayoutVisibility(toVisible = false)
    }

    private fun changeProductVariantPriceLayoutVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.productVariantPriceLayout?.visibility = viewVisibility
    }

    private fun showProductVariantOldPriceText(){
        changeProductOldPriceTextVisibility(toVisible = true)
    }

    private fun hideProductVariantOldPriceText(){
        changeProductOldPriceTextVisibility(toVisible = false)
    }

    private fun changeProductOldPriceTextVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.productVariantOldPriceText?.visibility = viewVisibility
    }

    private fun View.show(){
        this.visibility = View.VISIBLE
    }

    private fun View.hide(toGone: Boolean = true){
        visibility = if(toGone) View.GONE else View.INVISIBLE
    }

    private fun dismissAfterDelay(){
        Observable
            .timer(1L, TimeUnit.SECONDS, computationScheduler)
            .ignoreElements()
            .observeOn(mainThreadScheduler)
            .subscribeBy(
                onError = applicationErrorsLogger::logError,
                onComplete = ::dismiss
            )
            .addTo(viewScopeDisposables)
    }

    private fun navigateToProductsAreOrderedDialog(){
        val action = ProductOrderDialogFragmentDirections.actionToProductsAreOrderedDestination()
        navigationController.navigate(action)
    }

    private fun createDefaultProductDrawable(): Drawable?{
        return context?.getDrawableCompat(
            R.drawable.ic_baseline_shopping_bag_24,
            R.color.productOrder_productBoxItem_placeholderTint
        )
    }
}