package tv.wfc.livestreamsales.ui.activity.greeting.adapters.greetingpage

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.databinding.ItemGreetingPageBinding
import tv.wfc.livestreamsales.model.application.greetingpage.GreetingPage

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