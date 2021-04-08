package com.laurus.p.edittextformatters

class PhoneNumberTextFormatter(
    afterTextFormatted: ((noStyleText: CharSequence) -> Unit)? = null
): SeparatorsEditTextFormatter(afterTextFormatted) {
    companion object{
        private const val phoneNumberLength = 10
    }
    private val formattingSymbols = mapOf(
        0 to '(',
        4 to ')',
        5 to ' ',
        9 to '-',
        12 to '-'
    )

    @Synchronized
    override fun format(text: CharSequence, updateSeparatorsPositions: Boolean): CharSequence{
        val notFormattedText = text.take(phoneNumberLength)
        val result = StringBuilder(notFormattedText)

        formattingSymbols.forEach{ (formattingSymbolPosition, symbol) ->
            if(result.length > formattingSymbolPosition) {
                result.insert(formattingSymbolPosition, symbol)

                if (updateSeparatorsPositions) {
                    addSeparatorPosition(formattingSymbolPosition)
                }
            }
        }

        return result
    }
}