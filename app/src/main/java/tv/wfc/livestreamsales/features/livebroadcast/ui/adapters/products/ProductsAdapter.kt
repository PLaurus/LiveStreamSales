package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.products.Product

class ProductsAdapter(
    productsDiffUtilItemCallback: DiffUtil.ItemCallback<Product>,
    private val imageLoader: ImageLoader,
): ListAdapter<Product, ProductViewHolder>(productsDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val productPage =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_live_broadcast_product, parent, false)

        return ProductViewHolder(
            productPage,
            imageLoader
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}