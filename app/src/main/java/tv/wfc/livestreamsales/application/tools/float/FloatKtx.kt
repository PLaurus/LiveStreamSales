package tv.wfc.livestreamsales.application.tools.float

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Float.format(
    separator: Char = ' ',
    groupingSize: Int = 3,
    fractionDigitsCount: Int = 2
): String{
    val symbols = DecimalFormatSymbols()
    symbols.groupingSeparator = separator
    val df = DecimalFormat().apply{
        decimalFormatSymbols = symbols
        this.groupingSize = groupingSize
        maximumFractionDigits = fractionDigitsCount
        minimumFractionDigits = fractionDigitsCount
    }

    return df.format(this)
}