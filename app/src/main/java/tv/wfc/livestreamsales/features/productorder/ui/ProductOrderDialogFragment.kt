package tv.wfc.livestreamsales.features.productorder.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.LiveStreamSalesApplication
import tv.wfc.livestreamsales.application.ui.base.BaseDialogFragment
import tv.wfc.livestreamsales.databinding.DialogProductOrderBinding
import tv.wfc.livestreamsales.features.productorder.di.ProductOrderComponent
import tv.wfc.livestreamsales.features.productorder.viewmodel.IProductOrderViewModel
import javax.inject.Inject


class ProductOrderDialogFragment: BaseDialogFragment(R.layout.dialog_product_order){
    private var viewBinding: DialogProductOrderBinding? = null

    private lateinit var productOrderComponent: ProductOrderComponent

    @Inject
    override lateinit var viewModel: IProductOrderViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initializeProductOrderComponent()
        injectDependencies()
    }

    override fun onContentViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onContentViewCreated(view, savedInstanceState)
        bindView(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindView()
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

    private fun bindView(view: View){
        viewBinding = DialogProductOrderBinding.bind(view)
    }

    private fun unbindView(){
        viewBinding = null
    }
}