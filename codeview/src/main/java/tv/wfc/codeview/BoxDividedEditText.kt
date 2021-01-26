package tv.wfc.codeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.InputFilter
import android.text.InputType
import android.text.Selection
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.widget.addTextChangedListener
import kotlin.math.min


class BoxDividedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): AppCompatTextView(context, attrs, defStyleAttr){

    companion object{
        private const val DEFAULT_SQUARE_BOXES = true
        private const val DEFAULT_NUMBER_OF_BOXES = 4
    }

    private val inputFilters = mutableListOf<InputFilter>()

    private var textLengthFilter: InputFilter.LengthFilter? = null
        set(value){
            field?.let{
                inputFilters.remove(it)
            }
            field = value
            field?.let{
                inputFilters.add(it)
                updateInputFilters()
            }
        }

    private val defaultBoxWidth = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultBoxWidth
    )

    private val defaultBoxHeight = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultBoxHeight
    )

    private val defaultGapBetweenBoxesInPx = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultGapBetweenBoxes
    )

    private val defaultBoxBackgroundColor: Int = ContextCompat.getColor(
        context,
        R.color.boxDividedEditText_defaultBoxBackgroundColor
    )

    private val defaultFilledBoxBorderColor: Int = ContextCompat.getColor(
        context,
        R.color.boxDividedEditText_defaultFilledBoxBorderColor
    )

    private val defaultEmptyBoxBorderColor: Int = ContextCompat.getColor(
        context,
        R.color.boxDividedEditText_defaultEmptyBoxBorderColor
    )

    @Px
    private val defaultBoxBorderWidthInPx: Int = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultBoxBorderWidth
    )

    private val defaultEmptyBoxHint: String = resources.getString(
        R.string.boxDividedEditText_defaultEmptyBoxHint
    )

    private val defaultBoxCornerRadiusInPx: Int = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultBoxCornerRadius
    )

    private val defaultTextSize: Int = resources.getDimensionPixelSize(
        R.dimen.boxDividedEditText_defaultTextSize
    )

    private val defaultHintTextColor: Int = ContextCompat.getColor(
        context,
        R.color.boxDividedEditText_defaultHintTextColor
    )

    private val paddingHorizontal: Int
        get() = paddingStart + paddingEnd

    private val paddingVertical: Int
        get() = paddingTop + paddingBottom

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val boxRectangle = RectF(0f, 0f, 0f, 0f)
    private val boxBorderRectangle = RectF(0f, 0f, 0f, 0f)

    private var boxWidth = defaultBoxWidth
    private var boxHeight = defaultBoxHeight

    var squareBoxes: Boolean = DEFAULT_SQUARE_BOXES
        set(value){
            field = value
            invalidate()
            requestLayout()
        }

    var numberOfBoxes: Int = DEFAULT_NUMBER_OF_BOXES
        set(value){
            field = value
            limitTextLengthToNumberOfBoxes()
            invalidate()
            if(squareBoxes) requestLayout()
        }

    @Px
    var gapBetweenBoxes: Int = defaultGapBetweenBoxesInPx
        set(value){
            field = value
            invalidate()
        }

    var boxBackgroundColor: Int = defaultBoxBackgroundColor
        set(value){
            field = value
            invalidate()
        }

    var filledBoxBorderColor: Int = defaultFilledBoxBorderColor
        set(value){
            field = value
            invalidate()
        }

    var emptyBoxBorderColor: Int = defaultEmptyBoxBorderColor
        set(value){
            field = value
            invalidate()
        }

    var boxBorderWidth: Int = defaultBoxBorderWidthInPx
        set(value){
            field = value
            invalidate()
        }

    var emptyBoxHint: String = defaultEmptyBoxHint
        set(value){
            field = value
            invalidate()
        }

    @Px
    var boxCornerRadius: Int = defaultBoxCornerRadiusInPx
        set(value){
            field = value
            invalidate()
        }

    var onMaxTextEntered: ((text: String) -> Unit)? = null

    init{
        background = null
        setTextIsSelectable(false)
        isCursorVisible = false
        isFocusable = true
        isEnabled = true
        isClickable = true
        isFocusableInTouchMode = true
        inputType = InputType.TYPE_CLASS_NUMBER
        
        addTextChangedListener {
            it?.toString()?.let{ newText ->
                if(newText.length >= numberOfBoxes)
                    onMaxTextEntered?.invoke(newText)
            }
        }

        context.withStyledAttributes(attrs, R.styleable.BoxDividedEditText, defStyleAttr){
            squareBoxes = getBoolean(
                R.styleable.BoxDividedEditText_squareBoxes,
                DEFAULT_SQUARE_BOXES
            )
            numberOfBoxes = getInt(
                R.styleable.BoxDividedEditText_numberOfBoxes,
                DEFAULT_NUMBER_OF_BOXES
            )

            gapBetweenBoxes = getDimensionPixelSize(
                R.styleable.BoxDividedEditText_gapBetweenBoxes,
                defaultGapBetweenBoxesInPx
            )

            boxBackgroundColor = getColor(
                R.styleable.BoxDividedEditText_boxBackgroundColor,
                defaultBoxBackgroundColor
            )

            filledBoxBorderColor = getColor(
                R.styleable.BoxDividedEditText_filledBoxBorderColor,
                defaultFilledBoxBorderColor
            )

            emptyBoxBorderColor = getColor(
                R.styleable.BoxDividedEditText_emptyBoxBorderColor,
                defaultEmptyBoxBorderColor
            )

            boxBorderWidth = getDimensionPixelSize(
                R.styleable.BoxDividedEditText_boxBorderWidth,
                defaultBoxBorderWidthInPx
            )

            emptyBoxHint = getString(
                R.styleable.BoxDividedEditText_emptyBoxHint
            ) ?: defaultEmptyBoxHint

            boxCornerRadius = getDimensionPixelSize(
                R.styleable.BoxDividedEditText_boxCornerRadius,
                defaultBoxCornerRadiusInPx
            )

            setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getDimension(
                    R.styleable.BoxDividedEditText_android_textSize,
                    defaultTextSize.toFloat()
                )
            )

            setHintTextColor(
                getColor(
                    R.styleable.BoxDividedEditText_android_textColorHint,
                    defaultHintTextColor
                )
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        val offeredViewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val offeredViewHeight = MeasureSpec.getSize(heightMeasureSpec)
        calculateBoxSize(widthMeasureMode, heightMeasureMode, offeredViewWidth, offeredViewHeight)

        val viewWidth: Int
        val viewHeight: Int

        if(widthMeasureMode == MeasureSpec.EXACTLY && heightMeasureMode == MeasureSpec.EXACTLY){
            viewWidth = offeredViewWidth
            viewHeight = offeredViewHeight
        } else if(widthMeasureMode == MeasureSpec.AT_MOST &&
                heightMeasureMode == MeasureSpec.EXACTLY){
            viewWidth = calculateViewWidth(boxWidth)
            viewHeight = offeredViewHeight
        } else if(widthMeasureMode == MeasureSpec.EXACTLY &&
                heightMeasureMode == MeasureSpec.AT_MOST){
            viewWidth = offeredViewWidth
            viewHeight = calculateViewHeight(boxHeight)
        } else {
            viewWidth = calculateViewWidth(boxWidth)
            viewHeight = calculateViewHeight(boxHeight)
        }

        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val currentText = text?.toString() ?: ""
        require(currentText.length <= numberOfBoxes)

        repeat(numberOfBoxes){ boxPosition ->
            calculateBoxRectangle(boxPosition)
            drawBoxBackground(canvas, boxRectangle)

            calculateBoxBorderRectangle(boxRectangle)

            if(boxPosition < currentText.length){
                drawFilledBoxBorder(canvas, boxBorderRectangle)

                drawText(
                    canvas,
                    currentText[boxPosition].toString(),
                    boxRectangle
                )
            } else {
                drawEmptyBoxBorder(canvas, boxBorderRectangle)

                drawHintText(
                    canvas,
                    emptyBoxHint,
                    boxRectangle
                )
            }
        }
    }

    private fun drawHintText(canvas: Canvas, text: String, boxRectangle: RectF){
        paint.reset()

        paint.color = currentHintTextColor
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        paint.typeface = typeface

        val textPositionX = ((boxRectangle.right - boxRectangle.left) / 2 +
                boxRectangle.left)

        val textPositionY = (boxRectangle.top + (boxRectangle.bottom - boxRectangle.top) / 2) + paint.textSize / 3

        canvas.drawText(
            text,
            textPositionX,
            textPositionY,
            paint
        )
        paint.reset()
    }

    private fun drawText(canvas: Canvas, text: String, boxRectangle: RectF){
        paint.reset()

        paint.color = currentTextColor
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.textSize = textSize
        paint.typeface = typeface

        val textPositionX = ((boxRectangle.right - boxRectangle.left) / 2 +
                boxRectangle.left)

        val textPositionY = (boxRectangle.top + (boxRectangle.bottom - boxRectangle.top) / 2) + paint.textSize / 3

        canvas.drawText(
            text,
            textPositionX,
            textPositionY,
            paint
        )
        paint.reset()
    }

    private fun calculateBoxRectangle(boxPosition: Int){
        val boxBackgroundXStart = paddingStart + boxPosition * (boxWidth + gapBetweenBoxes)
        val boxBackgroundXEnd = boxBackgroundXStart + boxWidth
        val boxBackgroundYTop = paddingTop
        val boxBackgroundYBottom = boxBackgroundYTop + boxHeight

        boxRectangle.set(
            boxBackgroundXStart.toFloat(),
            boxBackgroundYTop.toFloat(),
            boxBackgroundXEnd.toFloat(),
            boxBackgroundYBottom.toFloat()
        )
    }

    private fun drawBoxBackground(canvas: Canvas, boxRectangle: RectF){
        paint.reset()

        paint.color = boxBackgroundColor
        paint.style = Paint.Style.FILL

        canvas.drawRoundRect(
            boxRectangle,
            boxCornerRadius.toFloat(),
            boxCornerRadius.toFloat(),
            paint
        )

        paint.reset()
    }

    private fun calculateBoxBorderRectangle(boxRectangle: RectF){
        val borderXStart = boxRectangle.left + boxBorderWidth.toFloat() / 2
        val borderXEnd = boxRectangle.right - boxBorderWidth.toFloat() / 2
        val borderYTop = boxRectangle.top + boxBorderWidth.toFloat() / 2
        val borderYBottom = boxRectangle.bottom - boxBorderWidth.toFloat() / 2

        boxBorderRectangle.set(
            borderXStart,
            borderYTop,
            borderXEnd,
            borderYBottom
        )
    }

    private fun drawFilledBoxBorder(
        canvas: Canvas,
        boxBorderRectangle: RectF
    ){
        drawBoxBorder(canvas, boxBorderRectangle, filledBoxBorderColor)
    }

    private fun drawEmptyBoxBorder(
        canvas: Canvas,
        boxBorderRectangle: RectF
    ){
        drawBoxBorder(canvas, boxBorderRectangle, emptyBoxBorderColor)
    }

    private fun drawBoxBorder(
        canvas: Canvas,
        boxBorderRectangle: RectF,
        @ColorInt
        color: Int
    ){
        paint.reset()

        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = boxBorderWidth.toFloat()

        canvas.drawRoundRect(
            boxBorderRectangle,
            boxCornerRadius.toFloat(),
            boxCornerRadius.toFloat(),
            paint
        )

        paint.reset()
    }

    private fun calculateBoxSize(
        widthMeasureMode: Int,
        heightMeasureMode: Int,
        offeredViewWidth: Int,
        offeredViewHeight: Int
    ){
        val boxMaxWidth = if(widthMeasureMode != MeasureSpec.UNSPECIFIED){
            calculateBoxMaxWidth(offeredViewWidth)
        } else null

        val boxMaxHeight = if(heightMeasureMode != MeasureSpec.UNSPECIFIED){
            calculateBoxMaxHeight(offeredViewHeight)
        } else null

        if(squareBoxes){
            val squaredBoxSideSize = if(boxMaxWidth != null && boxMaxHeight != null){
                min(boxMaxWidth, boxMaxHeight)
            } else boxMaxWidth ?: (boxMaxHeight ?: min(defaultBoxWidth, defaultBoxHeight))

            boxWidth = squaredBoxSideSize
            boxHeight = squaredBoxSideSize
        } else {
            boxWidth = boxMaxWidth ?: defaultBoxWidth
            boxHeight = boxMaxHeight ?: defaultBoxHeight
        }
    }

    private fun calculateBoxMaxWidth(maxViewWidth: Int): Int{
        return (maxViewWidth - paddingHorizontal - (numberOfBoxes - 1) * gapBetweenBoxes)/ numberOfBoxes
    }

    private fun calculateBoxMaxHeight(maxViewHeight: Int): Int{
        return maxViewHeight - paddingVertical
    }

    private fun calculateViewWidth(boxWidth: Int): Int{
        return boxWidth * numberOfBoxes + (numberOfBoxes - 1) * gapBetweenBoxes + paddingStart + paddingEnd
    }

    private fun calculateViewHeight(boxHeight: Int): Int{
        return boxHeight + paddingVertical
    }

    private fun limitTextLengthToNumberOfBoxes(){
        textLengthFilter = InputFilter.LengthFilter(numberOfBoxes)
    }

    private fun updateInputFilters(){
        filters = inputFilters.toTypedArray()
    }

    override fun isTextSelectable(): Boolean {
        return false
    }

    override fun setTextIsSelectable(selectable: Boolean) {
        super.setTextIsSelectable(false)
    }

    public override fun onSelectionChanged(start: Int, end: Int) {
        val text: CharSequence? = text
        if (text != null) {
            if (start != text.length || end != text.length) {
                Selection.setSelection(editableText, text.length, text.length)
                return
            }
        }

        super.onSelectionChanged(start, end)
    }
}