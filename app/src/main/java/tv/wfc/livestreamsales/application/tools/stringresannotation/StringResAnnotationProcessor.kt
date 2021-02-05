package tv.wfc.livestreamsales.application.tools.stringresannotation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.*
import android.text.Annotation
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.getSpans
import tv.wfc.livestreamsales.R
import javax.inject.Inject

class StringResAnnotationProcessor @Inject constructor() : IStringResAnnotationProcessor {
    private enum class Keys(val value: String){
        Link("link"),
        Highlighted("highlighted")
    }

    override fun process(context: Context, @StringRes resId: Int): SpannableString{
        val text = context.getText(resId)
        return process(context, text as SpannedString)
    }

    override fun process(context: Context, text: SpannedString): SpannableString {
        return with(context){
            val spannableString = SpannableString(text)
            val annotations = text.getSpans<Annotation>(0, text.length)

            annotations.forEach{ annotation ->
                val style = selectSpanFromAnnotation(annotation)

                spannableString.setSpan(
                    style,
                    text.getSpanStart(annotation),
                    text.getSpanEnd(annotation),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            spannableString
        }
    }

    private fun Context.selectSpanFromAnnotation(annotation: Annotation): CharacterStyle? {
        val key = annotation.key
        val value = annotation.value

        return when(key){
            Keys.Link.value -> getLinkSpan(value)
            Keys.Highlighted.value -> ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            else -> null
        }
    }

    private fun Context.getLinkSpan(url: String) = object: ClickableSpan(){
        override fun onClick(p0: View) {
            val webPage = Uri.parse(url)
            val webPageIntent = Intent(Intent.ACTION_VIEW, webPage)

            if(webPageIntent.resolveActivity(packageManager) != null){
                startActivity(webPageIntent)
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }
}