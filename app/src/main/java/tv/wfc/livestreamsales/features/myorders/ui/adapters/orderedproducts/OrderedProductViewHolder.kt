package tv.wfc.livestreamsales.features.myorders.ui.adapters.orderedproducts

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct
import tv.wfc.livestreamsales.databinding.ListItemMyOrdersOrderOrderedProductBinding

class OrderedProductViewHolder(
    orderedProductView: View,
    private val imageLoader: ImageLoader
): RecyclerView.ViewHolder(orderedProductView) {
    private val viewBinding = ListItemMyOrdersOrderOrderedProductBinding.bind(orderedProductView)
    private val context = orderedProductView.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(orderedProduct: OrderedProduct){
        clear()
        initializeCountInStockLayout(orderedProduct)
        initializeCountInStockText(orderedProduct)
        initializeImageView(orderedProduct)
    }

    private fun clear(){
        clearCountInStockLayout()
        clearCountInStockText()
        clearImageView()
    }

    private fun initializeCountInStockLayout(product: OrderedProduct){
        val countInStock = product.amount

        viewBinding.countInStockLayout.run{
            visibility = if(countInStock <= 0) View.GONE else View.VISIBLE
        }
    }

    private fun clearCountInStockLayout(){
        viewBinding.countInStockLayout.visibility = View.GONE
    }

    private fun initializeCountInStockText(product: OrderedProduct){
        viewBinding.countInStockText.text = product.amount.toString()
    }

    private fun clearCountInStockText(){
        viewBinding.countInStockText.text = ""
    }

    private fun initializeImageView(orderedProduct: OrderedProduct){
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
        viewBinding.imageView.setImageDrawable(drawable)
    }

    private fun setImageDefaultDrawable(){
        viewBinding.imageView.run{
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

    private fun clearImageView(){
        imageLoaderDisposable?.dispose()
        viewBinding.imageView.setImageDrawable(null)
    }
}