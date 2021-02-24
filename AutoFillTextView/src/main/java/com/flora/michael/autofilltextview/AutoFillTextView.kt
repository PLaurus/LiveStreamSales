package com.flora.michael.autofilltextview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.withStyledAttributes

class AutoFillTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatTextView(context, attrs, defStyleAttr) {
    private val defaultFiller = resources.getString(R.string.default_filler)
    private val fillerWidth: Float
        get() = getTextWidth(filler)

    var filler = defaultFiller
        set(value){
            field = value
            invalidate()
        }

    init{
        context.withStyledAttributes(attrs, R.styleable.AutoFillTextView, defStyleAttr){
            filler = getString(R.styleable.AutoFillTextView_filler) ?: defaultFiller
        }
    }

    private val stringOfFillersBuilder = StringBuilder()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val lineWidth = width - paddingLeft - paddingRight

        val lastLineWidth = layout.getLineWidth(lineCount - 1).toInt()
        val availableWidthForFillers = lineWidth - lastLineWidth

        val requiredDots = if (layout == null) {
            1
        } else (availableWidthForFillers / fillerWidth).toInt()

        if (requiredDots != 0) {
            stringOfFillersBuilder.clear()
            stringOfFillersBuilder.append(text.toString())

            for (i in 0 until requiredDots) {
                stringOfFillersBuilder.append(".")
            }

            text = stringOfFillersBuilder.toString()
        }

        super.onLayout(changed, left, top, right, bottom)
    }

    private fun getTextWidth(text: String): Float{
        return paint.measureText(text)
    }
}