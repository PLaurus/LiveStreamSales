package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.messages

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.application.model.chat.ChatMessage
import tv.wfc.livestreamsales.databinding.ItemChatMessageBinding

class MessageViewHolder(
    messageView: View
): RecyclerView.ViewHolder(messageView) {
    private val viewBinding = ItemChatMessageBinding.bind(messageView)

    fun bind(message: ChatMessage){
        clear()

        initializeChatMessageText(message)
    }

    private fun clear(){
        clearChatMessageText()
    }

    private fun initializeChatMessageText(message: ChatMessage){
        val spannableString = SpannableStringBuilder()
            .bold { append("${message.sender}: ") }
            .append(message.message)

        viewBinding.chatMessageText.text = spannableString
    }

    private fun clearChatMessageText(){
        viewBinding.chatMessageText.text = ""
    }
}