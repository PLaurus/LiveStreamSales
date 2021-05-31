package tv.wfc.livestreamsales.features.productorder.ui.adapters.cart

import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import com.laurus.p.tools.context.getDrawableCompat
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ItemProductInCartBinding
import tv.wfc.livestreamsales.application.model.products.order.ProductInCart

class ProductInCartViewHolder(
    view: View,
    private val imageLoader: ImageLoader,
    private val onDeleteClicked: (productId: Long) -> Unit,
    private val onProductInCartClicked: (productId: Long) -> Unit
): RecyclerView.ViewHolder(view) {
    private val viewBinding = ItemProductInCartBinding.bind(view)
    private val context = viewBinding.root.context

    private var imageLoaderDisposable: Disposable? = null

    fun bind(productInCart: ProductInCart){
        clearData()

        val productId = productInCart.product.id

        initializeDeleteButton(productId)
        initializeImageView(productId, productInCart.product.image)
        initializeAmountText(productInCart.amount)
    }

    private fun clearData(){
        clearDeleteButton()
        clearImageView()
        clearAmountText()
    }

    private fun clearDeleteButton(){
        viewBinding.removeFromCartButton.setOnClickListener(null)
    }

    private fun clearImageView(){
        imageLoaderDisposable?.dispose()
        viewBinding.imageView.setImageDrawable(null)
        viewBinding.imageCard.setOnClickListener(null)
    }

    private fun clearAmountText(){
        viewBinding.amountText.text = ""
    }

    private fun initializeDeleteButton(productId: Long){
        viewBinding.removeFromCartButton.setOnClickListener {
            onDeleteClicked(productId)
        }
    }

    private fun initializeImageView(productId: Long ,imageUrl: String?){
        if(imageUrl == null){
            setImageDefaultDrawable()
        } else{
            val imageRequest = ImageRequest.Builder(context)
                .data(imageUrl)
                .target(
                    onError = { setImageDefaultDrawable() },
                    onSuccess = ::setImageDrawable
                )
                .build()

            imageLoaderDisposable?.dispose()
            imageLoaderDisposable = imageLoader.enqueue(imageRequest)
        }

        viewBinding.imageView.setOnClickListener {
            onProductInCartClicked(productId)
        }
    }

    private fun initializeAmountText(amount: Int){
        viewBinding.amountText.text = amount.toString()
    }

    private fun setImageDrawable(drawable: Drawable){
        viewBinding.imageView.run {
            setImageDrawable(drawable)
        }
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
            R.color.productOrder_productBoxItem_placeholderTint
        )
    }
}