package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.products

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import com.laurus.p.tools.floatKtx.format
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.products.ProductGroup
import tv.wfc.livestreamsales.databinding.ListItemLiveBroadcastProductBinding

class ProductViewHolder(
    productPage: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(productPage)  {
    private val viewBinding = ListItemLiveBroadcastProductBinding.bind(productPage)
    private val context get() = viewBinding.root.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(product: ProductGroup){
        clear()
        initializeProductImageView(product)
        initializeProductsAmountLayout(product)
        initializeProductsAmountText(product)
        initializeProductNameText(product)
        initializeMinProductVariantPriceText(product)
    }

    private fun clear(){
        clearProductImageView()
        clearProductsAmountLayout()
        clearProductsAmountText()
        clearProductNameText()
        clearProductPriceText()
    }

    private fun initializeProductImageView(product: ProductGroup){
        val productImageUrl = product.image

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

    private fun clearProductImageView(){
        imageLoaderDisposable?.dispose()
        viewBinding.productImageView.setImageDrawable(null)
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

    private fun initializeProductsAmountLayout(product: ProductGroup){
        val amount = product.quantityInStock

        viewBinding.productsAmountLayout.run{
            visibility = if(amount == null || amount <= 0) View.GONE else View.VISIBLE
        }
    }

    private fun clearProductsAmountLayout(){
        viewBinding.productsAmountLayout.visibility = View.GONE
    }

    private fun initializeProductsAmountText(product: ProductGroup){
        val amount = product.quantityInStock ?: 0

        viewBinding.productsAmountText.run{
            visibility = if(amount <= 0) View.GONE else View.VISIBLE
            text = amount.toString()
        }
    }

    private fun clearProductsAmountText(){
        viewBinding.productsAmountText.run{
            visibility = View.GONE
            text = ""
        }
    }

    private fun initializeProductNameText(product: ProductGroup){
        viewBinding.productNameText.text = product.name
    }

    private fun clearProductNameText(){
        viewBinding.productNameText.text = ""
    }

    private fun initializeMinProductVariantPriceText(product: ProductGroup){
        val formattedPrice = product.minProductVariantPrice?.format()

        viewBinding.minProductVariantPriceText.run{
            if(formattedPrice != null){
                text = context.getString(
                    R.string.fragment_live_broadcast_price,
                    formattedPrice
                )
                visibility = View.VISIBLE
            } else { visibility = View.GONE }
        }
    }

    private fun clearProductPriceText(){
        viewBinding.minProductVariantPriceText.text = ""
    }
}