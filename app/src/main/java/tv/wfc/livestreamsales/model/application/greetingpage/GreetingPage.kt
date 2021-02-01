package tv.wfc.livestreamsales.model.application.greetingpage

import android.graphics.drawable.Drawable

data class GreetingPage(
    val pageOrder: Int,
    val image: Drawable?,
    val title: String,
    val description: String
)