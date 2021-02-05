package tv.wfc.livestreamsales.features.greeting.ui.adapters.greetingpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage
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