package tv.wfc.livestreamsales.features.productorder.ui.adapters.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.features.productorder.model.ProductBoxData

class ProductBoxesAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<ProductBoxData>,
    private val imageLoader: ImageLoader,
    private val onProductSelected: (productPosition: Int) -> Unit
): ListAdapter<ProductBoxData, ProductBoxViewHolder>(diffUtilItemCallback) {
    private val viewHolders = mutableListOf<ProductBoxViewHolder>()
    private var selectedProductBoxData: ProductBoxData? = null

    override fun onCurrentListChanged(previousList: MutableList<ProductBoxData>, currentList: MutableList<ProductBoxData>) {
        if(selectedProductBoxData == null){
            selectedProductBoxData = currentList.getOrNull(0)
        }
        super.onCurrentListChanged(previousList, currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBoxViewHolder {
        val productBoxView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_product_order_product_box, parent, false)

        val productViewHolder =  ProductBoxViewHolder(
            productBoxView,
            imageLoader,
            ::onItemClick
        )

        viewHolders.add(productViewHolder)
        return productViewHolder
    }

    override fun onBindViewHolder(holder: ProductBoxViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)

        if(item != selectedProductBoxData){
            holder.deselectItem()
        }
    }

    override fun onViewRecycled(holder: ProductBoxViewHolder) {
        viewHolders.remove(holder)
        super.onViewRecycled(holder)
    }

    private fun onItemClick(holder: ProductBoxViewHolder, productBoxData: ProductBoxData){
        selectItem(holder)
        selectedProductBoxData = productBoxData
        onProductSelected(productBoxData.position)
    }

    private fun selectItem(holder: ProductBoxViewHolder){
        viewHolders.forEach { it.deselectItem() }
        holder.selectItem()
    }
}