package tv.wfc.livestreamsales.features.productorder.ui.adapters.productspecifications

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.databinding.ItemProductSpecificationBinding

class ProductSpecificationViewHolder(
    view: View
): RecyclerView.ViewHolder(view){
    val viewBinding = ItemProductSpecificationBinding.bind(view)

    fun bind(name: String, value: String?){
        clearData()

        viewBinding.productSpecificationName.text = name
        viewBinding.productSpecificationValue.text = value ?: ""
    }

    fun bind(specification: Pair<String, String?>){
        bind(specification.first, specification.second)
    }

    private fun clearData(){
        viewBinding.productSpecificationName.text = ""
        viewBinding.productSpecificationValue.text = ""
    }
}