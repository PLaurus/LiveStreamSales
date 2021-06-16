package tv.wfc.livestreamsales.features.orderinformation.ui.adapters.oderedproducts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.orders.OrderedProduct

class OrderedProductsAdapter(
    orderedProductDiffUtilItemCallback: DiffUtil.ItemCallback<OrderedProduct>,
    private val imageLoader: ImageLoader
): ListAdapter<OrderedProduct, OrderedProductViewHolder>(orderedProductDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedProductViewHolder {
        val orderedProductView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_order_information_ordered_product, parent, false)

        return OrderedProductViewHolder(
            orderedProductView,
            imageLoader
        )
    }

    override fun onBindViewHolder(holder: OrderedProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}