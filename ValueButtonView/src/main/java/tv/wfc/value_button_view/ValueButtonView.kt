package tv.wfc.value_button_view

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import com.laurus.p.tools.view.dpToPixels
import com.laurus.p.tools.view.pixelsToDp
import com.laurus.p.tools.view.pixelsToSp
import tv.wfc.value_button_view.databinding.LayoutValueButtonBinding
import kotlin.math.roundToInt

class ValueButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val viewBinding: LayoutValueButtonBinding

    private companion object {
        const val DEFAULT_IS_LEFT_ICON_SHOWN = false
        const val DEFAULT_IS_RIGHT_ICON_SHOWN = true
        const val DEFAULT_IS_TOP_ICON_SHOWN = false
        const val DEFAULT_IS_BOTTOM_ICON_SHOWN = false
        const val DEFAULT_IS_CLICKABLE = true
        const val DEFAULT_IS_FOCUSABLE = true
    }

    @Suppress("MemberVisibilityCanBePrivate")
    var labelText: String
        /**
         * Returns label text.
         */
        get() = viewBinding.label.text.toString()
        /**
         * Sets label text.
         * @param newText new label text.
         */
        set(newText) {
            viewBinding.label.text = newText
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var valueText: String
        /**
         * Returns value text.
         */
        get() = viewBinding.value.text.toString()
        /**
         * Sets value text.
         * @param newText new value text.
         */
        set(newText) {
            viewBinding.value.text = newText
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var isLeftIconShown: Boolean
        /**
         * Returns is left icon is shown.
         * True - if it is shown (visible);
         * False - if it is not shown (is gone).
         */
        get() = viewBinding.leftIcon.visibility == View.VISIBLE
        /**
         * Changes shown state for left icon.
         * @param isShown must be true if left icon should be shown or false if it should not
         * be shown.
         */
        set(isShown) {
            viewBinding.leftIcon.visibility = getVisibilityIntForIcon(isShown)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var isRightIconShown: Boolean
        /**
         * Returns is right icon is shown.
         * True - if it is shown (visible);
         * False - if it is not shown (is gone).
         */
        get() = viewBinding.rightIcon.visibility == View.VISIBLE
        /**
         * Changes shown state for right icon.
         * @param isShown must be true if right icon should be shown or false if it should not
         * be shown.
         */
        set(isShown) {
            viewBinding.rightIcon.visibility = getVisibilityIntForIcon(isShown)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var isTopIconShown: Boolean
        /**
         * Returns is top icon is shown.
         * True - if it is shown (visible);
         * False - if it is not shown (is gone).
         */
        get() = viewBinding.topIcon.visibility == View.VISIBLE
        /**
         * Changes shown state for top icon.
         * @param isShown must be true if top icon should be shown or false if it should not
         * be shown.
         */
        set(isShown) {
            viewBinding.topIcon.visibility = getVisibilityIntForIcon(isShown)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var isBottomIconShown: Boolean
        /**
         * Returns is bottom icon is shown.
         * True - if it is shown (visible);
         * False - if it is not shown (is gone).
         */
        get() = viewBinding.bottomIcon.visibility == View.VISIBLE
        /**
         * Changes shown state for bottom icon.
         * @param isShown must be true if bottom icon should be shown or false if it should not
         * be shown.
         */
        set(isShown) {
            viewBinding.bottomIcon.visibility = getVisibilityIntForIcon(isShown)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var leftIconMarginEnd: Int
        /**
         * Returns space size between text and left icon in density independent pixels (DP).
         */
        get() {
            val spaceSizeInPx = (viewBinding.textContainer.layoutParams as LayoutParams).marginStart

            return spaceSizeInPx.pixelsToDp(resources).roundToInt()
        }
        /**
         * Sets space size between left icon and text in density independent pixels (DP).
         */
        set(marginEnd) {
            val spaceSizeInPx = marginEnd.dpToPixels(resources).roundToInt()

            (viewBinding.textContainer.layoutParams as LayoutParams).marginStart = spaceSizeInPx
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var rightIconMarginStart: Int
        /**
         * Returns space size between text and right icon in density independent pixels (DP).
         */
        get() {
            val spaceSizeInPx = (viewBinding.textContainer.layoutParams as LayoutParams).marginEnd

            return spaceSizeInPx.pixelsToDp(resources).roundToInt()
        }
        /**
         * Sets space size between right icon and text in density independent pixels (DP).
         */
        set(marginStart) {
            val spaceSizeInPx = marginStart.dpToPixels(resources).roundToInt()

            (viewBinding.textContainer.layoutParams as LayoutParams).marginEnd = spaceSizeInPx
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var topIconMarginBottom: Int
        /**
         * Returns space size between top icon and text in density independent pixels (DP).
         */
        get() {
            val spaceSizeInPx = (viewBinding.textContainer.layoutParams as LayoutParams).topMargin

            return spaceSizeInPx.pixelsToDp(resources).roundToInt()
        }
        /**
         * Sets space size between top icon and text in density independent pixels (DP).
         */
        set(marginBottom) {
            val spaceSizeInPx = marginBottom.dpToPixels(resources).roundToInt()

            viewBinding.textContainer.updateLayoutParams<LayoutParams> {
                topMargin = spaceSizeInPx
            }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var bottomIconMarginTop: Int
        /**
         * Returns space size between bottom icon and text in density independent pixels (DP).
         */
        get() {
            val spaceSizeInPx = (viewBinding.textContainer.layoutParams as LayoutParams).bottomMargin

            return spaceSizeInPx.pixelsToDp(resources).roundToInt()
        }
        /**
         * Sets space size between bottom icon and text in density independent pixels (DP).
         */
        set(marginTop) {
            val spaceSizeInPx = marginTop.dpToPixels(resources).roundToInt()

            viewBinding.textContainer.updateLayoutParams<LayoutParams> {
                bottomMargin = spaceSizeInPx
            }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var labelTextColor: Int
        /**
         * Returns label text color in 0xAARRGGBB format.
         */
        get() = viewBinding.label.currentTextColor
        /**
         * Sets label text color.
         * @param newColor new color in 0xAARRGGBB format.
         */
        set(newColor) {
            viewBinding.label.setTextColor(newColor)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var valueTextColor: Int
        /**
         * Returns value text color in 0xAARRGGBB format.
         */
        get() = viewBinding.value.currentTextColor
        /**
         * Sets value text color.
         * @param newColor new color in 0xAARRGGBB format.
         */
        set(newColor) {
            viewBinding.value.setTextColor(newColor)
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var gapBetweenLabelAndValue: Int
        /**
         * Returns gap size between label and value in density independent pixels (DP).
         */
        get() {
            val gapInPx = (viewBinding.value.layoutParams as LayoutParams).topMargin

            return gapInPx.pixelsToDp(resources).roundToInt()
        }
        /**
         * Sets gap size between label and value in density independent pixels (DP).
         * @param newGapSize gap size between label and value in density independent pixels (DP).
         */
        set(newGapSize) {
            val gapInPx = newGapSize.dpToPixels(resources).roundToInt()

            viewBinding.value.updateLayoutParams<LayoutParams> {
                topMargin = gapInPx
            }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var labelTextSize: Float
        /**
         * Returns label text size in scale independent pixels (SP).
         */
        get() {
            val sizeInPx = viewBinding.label.textSize

            return sizeInPx.pixelsToSp(resources)
        }
        /**
         * Sets label text size in scale independent pixels (SP).
         * @param newSize text size in scale independent pixels (SP).
         */
        set(newSize) {
            viewBinding.label.textSize = newSize
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var valueTextSize: Float
        /**
         * Returns value text size in scale independent pixels (SP).
         */
        get() {
            val sizeInPx = viewBinding.value.textSize

            return sizeInPx.pixelsToSp(resources)
        }
        /**
         * Sets value text size in scale independent pixels (SP).
         * @param newSize text size in scale independent pixels (SP).
         */
        set(newSize) {
            viewBinding.value.textSize = newSize
        }

    init {

        val layoutInflater = LayoutInflater.from(context)

        val mergedView = layoutInflater.inflate(R.layout.layout_value_button, this, true)
        viewBinding = LayoutValueButtonBinding.bind(mergedView)

        isClickable = true
        isFocusable = true

        context.withStyledAttributes(
            attrs,
            R.styleable.ValueButtonView,
            defStyleAttr,
            defStyleRes
        ) {
            initializeLabelText(styledAttributes = this)
            initializeValueText(styledAttributes = this)
            initializeIsLeftIconShown(styledAttributes = this)
            initializeIsRightIconShown(styledAttributes = this)
            initializeIsTopIconShown(styledAttributes = this)
            initializeIsBottomIconShown(styledAttributes = this)
            initializeLeftIconMarginEnd(styledAttributes = this)
            initializeRightIconMarginStart(styledAttributes = this)
            initializeTopIconMarginBottom(styledAttributes = this)
            initializeBottomIconMarginTop(styledAttributes = this)
            initializeLabelTextColor(styledAttributes = this)
            initializeValueTextColor(styledAttributes = this)
            initializeGapBetweenLabelAndValue(styledAttributes = this)
            initializeLabelTextSize(styledAttributes = this)
            initializeValueTextSize(styledAttributes = this)
            initializeIsClickable(styledAttributes = this)
            initializeIsFocusable(styledAttributes = this)
            initializeBackground(styledAttributes = this)
        }
    }

    private fun initializeLabelText(styledAttributes: TypedArray) {
        labelText = styledAttributes.getString(R.styleable.ValueButtonView_vbv_label_text) ?:
            context.getString(R.string.default_label_text)
    }

    private fun initializeValueText(styledAttributes: TypedArray) {
        valueText = styledAttributes.getString(R.styleable.ValueButtonView_vbv_value_text) ?:
            context.getString(R.string.default_value_text)
    }

    private fun initializeIsLeftIconShown(styledAttributes: TypedArray) {
        isLeftIconShown = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_vbv_leftIcon_isShown,
            DEFAULT_IS_LEFT_ICON_SHOWN
        )
    }

    private fun initializeIsRightIconShown(styledAttributes: TypedArray) {
        isRightIconShown = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_vbv_rightIcon_isShown,
            DEFAULT_IS_RIGHT_ICON_SHOWN
        )
    }

    private fun initializeIsTopIconShown(styledAttributes: TypedArray) {
        isTopIconShown = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_vbv_topIcon_isShown,
            DEFAULT_IS_TOP_ICON_SHOWN
        )
    }

    private fun initializeIsBottomIconShown(styledAttributes: TypedArray) {
        isBottomIconShown = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_vbv_bottomIcon_isShown,
            DEFAULT_IS_BOTTOM_ICON_SHOWN
        )
    }

    private fun initializeLeftIconMarginEnd(styledAttributes: TypedArray) {
        leftIconMarginEnd = styledAttributes.getDimensionPixelSize(
            R.styleable.ValueButtonView_vbv_leftIcon_marginEnd,
            resources.getDimensionPixelSize(R.dimen.default_left_icon_margin_end)
        ).pixelsToDp(resources).roundToInt()
    }

    private fun initializeRightIconMarginStart(styledAttributes: TypedArray) {
        rightIconMarginStart = styledAttributes.getDimensionPixelSize(
            R.styleable.ValueButtonView_vbv_rightIcon_marginStart,
            resources.getDimensionPixelSize(R.dimen.default_right_icon_margin_start)
        ).pixelsToDp(resources).roundToInt()
    }

    private fun initializeTopIconMarginBottom(styledAttributes: TypedArray) {
        topIconMarginBottom = styledAttributes.getDimensionPixelSize(
            R.styleable.ValueButtonView_vbv_topIcon_marginBottom,
            resources.getDimensionPixelSize(R.dimen.default_top_icon_margin_bottom)
        ).pixelsToDp(resources).roundToInt()
    }

    private fun initializeBottomIconMarginTop(styledAttributes: TypedArray) {
        bottomIconMarginTop = styledAttributes.getDimensionPixelSize(
            R.styleable.ValueButtonView_vbv_bottomIcon_marginTop,
            resources.getDimensionPixelSize(R.dimen.default_bottom_icon_margin_top)
        ).pixelsToDp(resources).roundToInt()
    }

    private fun initializeLabelTextColor(styledAttributes: TypedArray) {
        labelTextColor = styledAttributes.getColor(
            R.styleable.ValueButtonView_vbv_label_textColor,
            ContextCompat.getColor(context, R.color.default_label_text_color)
        )
    }

    private fun initializeValueTextColor(styledAttributes: TypedArray) {
        valueTextColor = styledAttributes.getColor(
            R.styleable.ValueButtonView_vbv_value_textColor,
            ContextCompat.getColor(context, R.color.default_value_text_color)
        )
    }

    private fun initializeGapBetweenLabelAndValue(styledAttributes: TypedArray) {
        gapBetweenLabelAndValue = styledAttributes.getDimensionPixelSize(
            R.styleable.ValueButtonView_vbv_gapBetweenLabelAndValue,
            resources.getDimensionPixelSize(R.dimen.default_gap_between_label_and_value)
        ).pixelsToDp(resources).roundToInt()
    }

    private fun initializeLabelTextSize(styledAttributes: TypedArray) {
        labelTextSize = styledAttributes.getDimension(
            R.styleable.ValueButtonView_vbv_label_textSize,
            resources.getDimension(R.dimen.default_label_text_size)
        ).pixelsToSp(resources)
    }

    private fun initializeValueTextSize(styledAttributes: TypedArray) {
        valueTextSize = styledAttributes.getDimension(
            R.styleable.ValueButtonView_vbv_value_textSize,
            resources.getDimension(R.dimen.default_value_text_size)
        ).pixelsToSp(resources)
    }

    private fun initializeIsClickable(styledAttributes: TypedArray) {
        isClickable = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_android_clickable,
            DEFAULT_IS_CLICKABLE
        )
    }

    private fun initializeIsFocusable(styledAttributes: TypedArray) {
        isFocusable = styledAttributes.getBoolean(
            R.styleable.ValueButtonView_android_focusable,
            DEFAULT_IS_FOCUSABLE
        )
    }

    private fun initializeBackground(styledAttributes: TypedArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if(!styledAttributes.hasValueOrEmpty(R.styleable.ValueButtonView_android_background)) {
                setDefaultBackground()
            }
        } else {
            if(!styledAttributes.hasValue(R.styleable.ValueButtonView_android_background)) {
                setDefaultBackground()
            }
        }
    }

    private fun setDefaultBackground(){
        val selectableItemBackgroundTypedValue = TypedValue()

        val isAttributeFound = context.theme.resolveAttribute(
            android.R.attr.selectableItemBackgroundBorderless,
            selectableItemBackgroundTypedValue,
            true
        )

        if(isAttributeFound) {
            setBackgroundResource(selectableItemBackgroundTypedValue.resourceId)
        }
    }

    private fun getVisibilityIntForIcon(isVisible: Boolean): Int {
        return if(isVisible) View.VISIBLE else View.GONE
    }
}