package com.example.livestreamsales.ui.activity.greeting.adapters.greetingpage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamsales.databinding.ItemGreetingPageBinding
import com.example.livestreamsales.model.application.greetingpage.GreetingPage

class GreetingPageViewHolder(greetingPageView: View): RecyclerView.ViewHolder(greetingPageView){
    private val viewBinding = ItemGreetingPageBinding.bind(greetingPageView)

    fun bind(greetingPage: GreetingPage){
        viewBinding.apply{
            image.setImageDrawable(greetingPage.image)
            title.text = greetingPage.title
            description.text = greetingPage.description
        }
    }
}