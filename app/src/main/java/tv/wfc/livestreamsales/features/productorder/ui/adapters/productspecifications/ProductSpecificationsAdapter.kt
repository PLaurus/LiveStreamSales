package tv.wfc.livestreamsales.features.productorder.ui.adapters.productspecifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.R

class ProductSpecificationsAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<Pair<String, String?>>
): ListAdapter<Pair<String, String?>, ProductSpecificationViewHolder>(diffUtilItemCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductSpecificationViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product_specification, parent, false)

        return ProductSpecificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductSpecificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}