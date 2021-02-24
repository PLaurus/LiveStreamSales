package tv.wfc.livestreamsales.features.productorder.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.load
import coil.request.Disposable
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.tools.errors.IApplicationErrorsLogger
import tv.wfc.livestreamsales.application.tools.float.format
import tv.wfc.livestreamsales.application.tools.string.strikeThrough
import tv.wfc.livestreamsales.application.ui.base.BaseDialogFragment
import tv.wfc.livestreamsales.databinding.DialogProductOrderBinding
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.productorder.di.modules.diffutils.qualifiers.ProductSpecificationsDiffUtilItemCallback
import tv.wfc.livestreamsales.features.productorder.ui.adapters.productspecifications.ProductSpecificationsAdapter
import tv.wfc.livestreamsales.features.productorder.viewmodel.IProductOrderViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ProductOrderDialogFragment: BaseDialogFragment(R.layout.dialog_product_order){
    private val navigationArguments by navArgs<ProductOrderDialogFragmentArgs>()

    private var viewBinding: DialogProductOrderBinding? = null
    private var oneProductImageLoaderDisposable: Disposable? = null

    private lateinit var productOrderComponent: ProductOrderComponent

    @Inject
    override lateinit var viewModel: IProductOrderViewModel

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
    lateinit var productSpecificationsDiffUtilItemCallback: DiffUtil.ItemCallback<Pair<String, String?>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeProductOrderComponent()
        injectDependencies()
        prepareViewModel(navigationArguments.liveBroadcastId)
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDataIsPrepared() {
        super.onDataIsPrepared()
        initializeCloseButton()
        initializeHeaderLayout()
        initializeOneProductImage()
        initializeOneProductNameText()
        initializeSeveralProductsDescriptionText()
        initializeProductSpecificationsLayout()
        initializeProductSpecificationsRecyclerView()
        initializeProductPriceText()
        initializeProductOldPriceText()
    }

    override fun onDataPreparationFailure() {
        super.onDataPreparationFailure()
        dismissAfterDelay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
        oneProductImageLoaderDisposable?.dispose()
    }

    private fun initializeProductOrderComponent(){
        productOrderComponent = (context?.applicationContext as? LiveStreamSalesApplication)
            ?.appComponent
            ?.authorizationRepository()
            ?.authorizedUserComponent
            ?.productOrderComponent()
            ?.create(this)
            ?: throw IllegalStateException("User MUST be Authorized to use ${this::class.simpleName} class.")
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
            scaleType = ImageView.ScaleType.CENTER_CROP

            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                oneProductImageLoaderDisposable?.dispose()

                val productUrl = product.imageUrl

                oneProductImageLoaderDisposable = load(productUrl, imageLoader) {
                    listener(
                        onSuccess = { _, _ -> showOneProductImage() },
                        onError = { _, throwable ->
                            hideOneProductImage()
                            applicationErrorsLogger.logError(throwable)
                        }
                    )
                }
            })
        }
    }

    private fun initializeOneProductNameText(){
        viewBinding?.oneProductName?.run{
            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                text = product.name
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
        }
    }

    private fun initializeProductSpecificationsLayout(){
        viewBinding?.productSpecificationsLayout?.run{
            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                if (product.characteristics == null || product.characteristics.isEmpty()) {
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

            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                val specifications = product.characteristics?.toList()
                (adapter as ProductSpecificationsAdapter).submitList(specifications)
            })
        }
    }

    private fun initializeProductPriceText(){
        viewBinding?.productPriceText?.run{
            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                text = resources.getString(
                    R.string.fragment_product_order_price,
                    product.price.format()
                )
            })
        }
    }

    private fun initializeProductOldPriceText(){
        viewBinding?.productOldPriceText?.run {
            viewModel.selectedProduct.observe(viewLifecycleOwner, { product ->
                val productOldPrice = product.oldPrice

                if (productOldPrice == null) {
                    hideProductOldPriceText()
                } else {
                    text = getString(
                        R.string.fragment_product_order_price,
                        productOldPrice.format()
                    ).strikeThrough()
                    showProductOldPriceText()
                }
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

    private fun showOneProductImage(){
        changeOneProductImageVisibility(toVisible = true)
    }

    private fun hideOneProductImage(){
        changeOneProductImageVisibility(toVisible = false)
    }

    private fun changeOneProductImageVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.oneProductImage?.visibility = viewVisibility
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

    private fun showProductOldPriceText(){
        changeProductOldPriceTextVisibility(toVisible = true)
    }

    private fun hideProductOldPriceText(){
        changeProductOldPriceTextVisibility(toVisible = false)
    }

    private fun changeProductOldPriceTextVisibility(toVisible: Boolean){
        val viewVisibility = if(toVisible) View.VISIBLE else View.GONE
        viewBinding?.productOldPriceText?.visibility = viewVisibility
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
}