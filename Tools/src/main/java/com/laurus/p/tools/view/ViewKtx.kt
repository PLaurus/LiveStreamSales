package com.laurus.p.tools.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView

fun View.matchRootView(
    matchWidth: Boolean = true,
    matchHeight: Boolean = true
) = matchAnotherView(rootView, matchWidth, matchHeight)

fun View.matchAnotherView(
    anotherView: View,
    matchWidth: Boolean = true,
    matchHeight: Boolean = true
){
    layoutParams = layoutParams.apply {
        if(matchWidth){
            val rootViewWidth = anotherView.width
            width = rootViewWidth
        }

        if(matchHeight){
            val rootViewHeight = anotherView.height
            height = rootViewHeight
        }
    }
}

fun TextView.makeTextViewLinkable(){
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.removeHighlight(){
    highlightColor = Color.TRANSPARENT
}

fun View.changeVisibilitySmoothly(
    to: Visibility,
    animationDuration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
){
    when(to){
        Visibility.Visible -> revealSmoothly(animationDuration)
        Visibility.Invisible -> hideSmoothly(animationDuration, toGone = false)
        Visibility.Gone -> hideSmoothly(animationDuration)
    }
}

fun View.revealSmoothly(
    animationDuration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
){
    visibility = View.VISIBLE

    animate()
        .alpha(1f)
        .setDuration(animationDuration)
        .setListener(null)
}

fun View.hideSmoothly(
    animationDuration: Long = resources.getInteger(android.R.integer.config_shortAnimTime).toLong(),
    toGone: Boolean = true
){
    animate()
        .alpha(0f)
        .setDuration(animationDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = if(toGone) View.GONE else View.INVISIBLE
            }
        })
}

enum class Visibility{
    Visible,
    Invisible,
    Gone
}