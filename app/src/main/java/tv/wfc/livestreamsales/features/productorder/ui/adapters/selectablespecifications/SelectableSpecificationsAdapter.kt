package tv.wfc.livestreamsales.features.productorder.ui.adapters.selectablespecifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.features.productorder.model.SelectableSpecification

class SelectableSpecificationsAdapter(
    diffUtilItemCallback: DiffUtil.ItemCallback<SelectableSpecification<*>>,
    private val onSpecificationSelected: (selectableSpecIndex: Int, valueIndex: Int) -> Unit
): ListAdapter<SelectableSpecification<*>, SelectableSpecificationViewHolder>(diffUtilItemCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectableSpecificationViewHolder {
        val selectableSpecificationView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_product_order_selectable_specification, parent, false)

        return SelectableSpecificationViewHolder(selectableSpecificationView,)
    }

    override fun onBindViewHolder(holder: SelectableSpecificationViewHolder, position: Int) {
        holder.bind(getItem(position)){ valueIndex ->
            onSpecificationSelected(position, valueIndex)
        }
    }
}