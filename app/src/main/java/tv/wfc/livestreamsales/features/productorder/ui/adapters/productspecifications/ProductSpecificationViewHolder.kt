package tv.wfc.livestreamsales.features.productorder.ui.adapters.productspecifications

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.application.model.products.specification.Specification
import tv.wfc.livestreamsales.databinding.ItemProductSpecificationBinding

class ProductSpecificationViewHolder(
    view: View
): RecyclerView.ViewHolder(view){
    val viewBinding = ItemProductSpecificationBinding.bind(view)

    fun bind(name: String, description: String?){
        clearData()

        viewBinding.productSpecificationName.text = name
        viewBinding.productSpecificationValue.text = description ?: ""
    }

    fun bind(specification: Specification<*>){
        if(specification is Specification.DescriptiveSpecification){
            bind(specification.name, specification.value)
        }
    }

    private fun clearData(){
        viewBinding.productSpecificationName.text = ""
        viewBinding.productSpecificationValue.text = ""
    }
}