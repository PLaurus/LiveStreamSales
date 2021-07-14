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
import tv.wfc.livestreamsales.application.model.products.Product
import tv.wfc.livestreamsales.databinding.ListItemLiveBroadcastProductBinding

class ProductViewHolder(
    productPage: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(productPage)  {
    private val viewBinding = ListItemLiveBroadcastProductBinding.bind(productPage)
    private val context get() = viewBinding.root.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(product: Product){
        clear()
        initializeProductImageView(product)
        initializeProductsAmountLayout(product)
        initializeProductsAmountText(product)
        initializeProductNameText(product)
        initializeProductPriceText(product)
    }

    private fun clear(){
        clearProductImageView()
        clearProductsAmountLayout()
        clearProductsAmountText()
        clearProductNameText()
        clearProductPriceText()
    }

    private fun initializeProductImageView(product: Product){
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

    private fun initializeProductsAmountLayout(product: Product){
        val amount = product.quantityInStock

        viewBinding.productsAmountLayout.run{
            visibility = if(amount == null || amount <= 0) View.GONE else View.VISIBLE
        }
    }

    private fun clearProductsAmountLayout(){
        viewBinding.productsAmountLayout.visibility = View.GONE
    }

    private fun initializeProductsAmountText(product: Product){
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

    private fun initializeProductNameText(product: Product){
        viewBinding.productNameText.text = product.name
    }

    private fun clearProductNameText(){
        viewBinding.productNameText.text = ""
    }

    private fun initializeProductPriceText(product: Product){
        val formattedPrice = product.price.format()

        viewBinding.productPriceText.text = context.getString(
            R.string.fragment_live_broadcast_price,
            formattedPrice
        )
    }

    private fun clearProductPriceText(){
        viewBinding.productPriceText.text = ""
    }
}