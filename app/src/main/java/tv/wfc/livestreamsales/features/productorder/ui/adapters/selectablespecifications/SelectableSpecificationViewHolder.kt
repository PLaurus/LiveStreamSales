package tv.wfc.livestreamsales.features.productorder.ui.adapters.selectablespecifications

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.databinding.ListItemProductOrderSelectableSpecificationBinding
import tv.wfc.livestreamsales.features.productorder.model.SelectableSpecification

class SelectableSpecificationViewHolder(
    view: View
): RecyclerView.ViewHolder(view) {
    private val viewBinding = ListItemProductOrderSelectableSpecificationBinding.bind(view)
    private val context = viewBinding.root.context

    private val chipColorStates = arrayOf(
        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf()
    )

    fun bind(
        selectableSpecification: SelectableSpecification<*>,
        onSpecificationSelected: (valuePosition: Int) -> Unit
    ){
        clearView()
        initializeName(selectableSpecification.name)
        initializeChips(selectableSpecification)
        initializeChipGroup(onSpecificationSelected)
    }

    private fun initializeName(name: String){
        viewBinding.name.text = name
    }

    private fun initializeChips(
        selectableSpecification: SelectableSpecification<*>
    ){
        selectableSpecification.availableValues.forEach { availableValue ->
            val chip = when(selectableSpecification){
                is SelectableSpecification.ColorSpecification -> {
                    availableValue as Int

                    createColorChip(availableValue)
                }
                is SelectableSpecification.DescriptiveSpecification -> {
                    availableValue as String
                    createTextChip(availableValue)
                }
            }

            viewBinding.specificationsChipGroup.addView(chip)
            if(availableValue == selectableSpecification.selectedValue){
                chip.isChecked = true
            }
        }


    }

    private fun initializeChipGroup(
        onSpecificationSelected: (valuePosition: Int) -> Unit
    ){
        viewBinding.specificationsChipGroup.apply {
            setOnCheckedChangeListener { _, checkedId ->
                val checkedPosition = children.indexOfFirst { it.id == checkedId }
                onSpecificationSelected(checkedPosition)
            }
        }
    }

    private fun clearView(){
        clearName()
        clearChipGroup()
    }

    private fun clearName(){
        viewBinding.name.text = ""
    }

    private fun clearChipGroup(){
        viewBinding.specificationsChipGroup.run{
            removeAllViews()
            setOnCheckedChangeListener(null)
        }

    }

    private fun createColorChip(
        @ColorInt
        color: Int
    ): Chip{
        return createChoiceChip().apply {
            layoutParams = ChipGroup.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.productOrder_selectableSpecification_colorChipWidth),
                resources.getDimensionPixelSize(R.dimen.productOrder_selectableSpecification_chipHeight)
            )
            chipBackgroundColor = createColorChipBackground(color)
            chipStrokeColor = createColorChipStroke()
            chipStrokeWidth = resources.getDimension(R.dimen.productOrder_selectableSpecification_colorChip_width)
        }
    }

    private fun createTextChip(text: String): Chip{
        return createChoiceChip().apply {
            layoutParams = ChipGroup.LayoutParams(
                ChipGroup.LayoutParams.WRAP_CONTENT,
                resources.getDimensionPixelSize(R.dimen.productOrder_selectableSpecification_chipHeight)
            )
            this.text = text
        }
    }

    private fun createChoiceChip(): Chip{
        return Chip(context).apply {
            setChipDrawable(ChipDrawable.createFromAttributes(
                context,
                null,
                0,
                R.style.Widget_MaterialComponents_Chip_Choice
            ))
            setEnsureMinTouchTargetSize(false)
        }
    }

    private fun createColorChipBackground(@ColorInt color: Int): ColorStateList{
        val disabledColor = ColorUtils.setAlphaComponent(color, 25)
        return ColorStateList(
            chipColorStates,
            intArrayOf(
                color,
                color,
                color,
                disabledColor
            )
        )
    }

    private fun createColorChipStroke(): ColorStateList{
        val selectedColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val checkedColor = ContextCompat.getColor(context, R.color.colorPrimary)
        val enabledColor = ContextCompat.getColor(context, R.color.colorSecondaryText)
        val disabledColor = ColorUtils.setAlphaComponent(enabledColor, 25)

        return ColorStateList(
            chipColorStates,
            intArrayOf(
                selectedColor,
                checkedColor,
                enabledColor,
                disabledColor
            )
        )
    }
}