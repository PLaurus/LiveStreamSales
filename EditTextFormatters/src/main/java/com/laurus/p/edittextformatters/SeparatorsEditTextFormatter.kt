package com.laurus.p.edittextformatters

import android.text.Editable
import androidx.annotation.CallSuper

abstract class SeparatorsEditTextFormatter(
    private val afterTextFormatted: ((noStyleText: CharSequence) -> Unit)?
): IEditTextFormatter {
    private var selfChanged = false
    private val separatorsPositions: MutableList<Int> = mutableListOf()

    final override fun format(text: CharSequence): CharSequence {
        return clearSeparatorsAndFormat(text, updateSeparatorsPositions = false)
    }

    @Synchronized
    @CallSuper
    override fun beforeTextChanged(
        currentText: CharSequence?,
        changeStart: Int,
        countOfSymbolsToBeReplaced: Int,
        replacementLength: Int
    ){
        if(selfChanged) return

        val changeEnd = changeStart + countOfSymbolsToBeReplaced

        val uselessSeparatorsPositions = mutableListOf<Int>()

        separatorsPositions.forEach { separatorPosition ->
            if(separatorPosition in changeStart..changeEnd){
                uselessSeparatorsPositions.add(separatorPosition)
            }
        }

        uselessSeparatorsPositions.forEach { uselessPosition ->
            separatorsPositions.removeAll { it == uselessPosition }
        }
    }

    @Synchronized
    @CallSuper
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    @Synchronized
    @CallSuper
    override fun afterTextChanged(s: Editable?) {
         if(selfChanged){
            selfChanged = false
            return
        }

        s?.run{
            val previousText = s.toString()
            val previousNotFormatted = previousText.removeSeparators()
            val formattedText = clearSeparatorsAndFormat(previousNotFormatted, updateSeparatorsPositions = true)
            val savedFilters = filters
            filters = arrayOf()
            selfChanged = true
            s.replace(0, s.length, formattedText)
            filters = savedFilters
            afterTextFormatted?.invoke(previousNotFormatted)
        }
    }

    protected abstract fun format(text: CharSequence, updateSeparatorsPositions: Boolean): CharSequence

    protected fun addSeparatorPosition(position: Int){
        separatorsPositions.add(position)
    }

    private fun CharSequence.removeSeparators(): CharSequence{
        var result = this

        separatorsPositions.forEachIndexed { index, position ->
            val startPosition = position - index
            result = result.replaceRange(startPosition, startPosition + 1, "")
        }

        return result
    }

    @Synchronized
    private fun clearSeparatorsAndFormat(text: CharSequence, updateSeparatorsPositions: Boolean): CharSequence{
        if(updateSeparatorsPositions){
            separatorsPositions.clear()
        }

        return format(text, updateSeparatorsPositions)
    }
}