package tv.wfc.livestreamsales.features.productorder.ui.adapters.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

class ProductsInCartAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>,
    private val imageLoader: ImageLoader,
    private val onDeleteProductFromCart: (productId: Long) -> Unit,
    private val onProductInCartSelected: (productId: Long) -> Unit
): ListAdapter<OrderedProduct, ProductInCartViewHolder>(diffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInCartViewHolder {
        val productInCartView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product_in_cart, parent, false)

        return ProductInCartViewHolder(
            productInCartView,
            imageLoader,
            onDeleteProductFromCart,
            onProductInCartSelected
        )
    }

    override fun onBindViewHolder(holder: ProductInCartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}