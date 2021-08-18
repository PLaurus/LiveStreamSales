package tv.wfc.livestreamsales.features.orderinformation.ui.adapters.oderedproducts

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import com.laurus.p.tools.floatKtx.format
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.databinding.ListItemOrderInformationOrderedProductBinding

class OrderedProductViewHolder(
    orderedProductView: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(orderedProductView) {
    private val viewBinding = ListItemOrderInformationOrderedProductBinding.bind(orderedProductView)
    private val context = orderedProductView.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(orderedProduct: OrderedProduct){
        clear()
        initializeProductImageView(orderedProduct)
        initializeProductsAmountLayout(orderedProduct)
        initializeProductsAmountText(orderedProduct)
        initializeProductNameText(orderedProduct)
        initializeSpecificationsTextView(orderedProduct)
        initializeProductPriceText(orderedProduct)
    }

    private fun clear(){
        clearProductImageView()
        clearProductsAmountLayout()
        clearProductsAmountText()
        clearProductNameText()
        clearSpecificationsTextView()
        clearProductPriceText()
    }

    private fun initializeProductImageView(orderedProduct: OrderedProduct){
        val productImageUrl = orderedProduct.product.image

        if(productImageUrl == null){
            setImageDefaultDrawable()
        } else{
            val imageRequest = ImageRequest.Builder(context)
                .data(productImageUrl)
                .target(
                    onError = { setImageDefaultDrawable() },
                    onSuccess = ::setImageDrawable
                )
                .build()

            imageLoaderDisposable?.dispose()
            imageLoaderDisposable = imageLoader.enqueue(imageRequest)
        }
    }

    private fun setImageDrawable(drawable: Drawable){
        viewBinding.productImageView.setImageDrawable(drawable)
    }

    private fun setImageDefaultDrawable(){
        viewBinding.productImageView.run{
            val placeholder = createImageDefaultDrawable()
            setImageDrawable(placeholder)
        }
    }

    private fun createImageDefaultDrawable(): Drawable?{
        return context.getDrawableCompat(
            R.drawable.ic_baseline_shopping_bag_24,
            R.color.color_black
        )
    }

    private fun clearProductImageView(){
        imageLoaderDisposable?.dispose()
        viewBinding.productImageView.setImageDrawable(null)
    }

    private fun initializeProductsAmountLayout(orderedProduct: OrderedProduct){
        val amount = orderedProduct.amount

        viewBinding.productsAmountLayout.run{
            visibility = if(amount <= 0) View.GONE else View.VISIBLE
        }
    }

    private fun clearProductsAmountLayout(){
        viewBinding.productsAmountLayout.visibility = View.GONE
    }

    private fun initializeProductsAmountText(orderedProduct: OrderedProduct){
        viewBinding.productsAmountText.run{
            visibility = if(orderedProduct.amount <= 0) View.GONE else View.VISIBLE
            text = orderedProduct.amount.toString()
        }
    }

    private fun clearProductsAmountText(){
        viewBinding.productsAmountText.run{
            visibility = View.GONE
            text = ""
        }
    }

    private fun initializeProductNameText(orderedProduct: OrderedProduct){
        viewBinding.productNameText.text = orderedProduct.product.name
    }

    private fun clearProductNameText(){
        viewBinding.productNameText.text = ""
    }

    private fun initializeSpecificationsTextView(orderedProduct: OrderedProduct){
        val productSpecificationsInOneString = StringBuilder()

        orderedProduct.product.specifications.forEach { specification ->
            val specificationValue: String = when(specification){
                is Specification.ColorSpecification ->{
                    specification.colorName
                }
                is Specification.DescriptiveSpecification ->{
                    specification.value
                }
            }

            productSpecificationsInOneString
                .append(specification.name)
                .append(": ")
                .append(specificationValue)
                .append("; ")
        }

        viewBinding.specificationsTextView.text = productSpecificationsInOneString.toString()
    }

    private fun clearSpecificationsTextView(){
        viewBinding.specificationsTextView.text = ""
    }

    private fun initializeProductPriceText(orderedProduct: OrderedProduct){
        val unitPrice = orderedProduct.product.price
        val amount = orderedProduct.amount
        val sumPrice = unitPrice * amount

        viewBinding.productPriceText.text = context.getString(
            R.string.fragment_order_information_sum_text,
            sumPrice.format()
        )
    }

    private fun clearProductPriceText(){
        viewBinding.productPriceText.text = ""
    }
}