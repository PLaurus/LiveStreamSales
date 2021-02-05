package tv.wfc.livestreamsales.features.greeting.ui.adapters.greetingpage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.databinding.ItemGreetingPageBinding
import tv.wfc.livestreamsales.features.greeting.model.GreetingPage

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