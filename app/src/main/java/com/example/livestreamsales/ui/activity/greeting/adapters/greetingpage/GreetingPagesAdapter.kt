package com.example.livestreamsales.ui.activity.greeting.adapters.greetingpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.livestreamsales.R
import com.example.livestreamsales.model.application.greetingpage.GreetingPage
import javax.inject.Inject

class GreetingPagesAdapter @Inject constructor(
    greetingPagesDiffUtilCallback: DiffUtil.ItemCallback<GreetingPage>
): ListAdapter<GreetingPage, GreetingPageViewHolder>(greetingPagesDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GreetingPageViewHolder {
        val greetingPageView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_greeting_page, parent, false)

        return GreetingPageViewHolder(greetingPageView)
    }

    override fun onBindViewHolder(holder: GreetingPageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}