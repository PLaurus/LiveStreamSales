package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.messages

import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage
import tv.wfc.livestreamsales.databinding.ItemChatMessageBinding
import java.lang.StringBuilder

class StreamChatMessageViewHolder(
    streamChatMessageView: View
): RecyclerView.ViewHolder(streamChatMessageView) {
    private val viewBinding = ItemChatMessageBinding.bind(streamChatMessageView)

    fun bind(message: StreamChatMessage){
        clear()

        initializeChatMessageText(message)
    }

    private fun clear(){
        clearChatMessageText()
    }

    private fun initializeChatMessageText(message: StreamChatMessage){
        val spannableString = SpannableStringBuilder()
            .bold { append("${createSenderName(message)}: ") }
            .append(message.text)

        viewBinding.chatMessageText.text = spannableString
    }

    private fun clearChatMessageText(){
        viewBinding.chatMessageText.text = ""
    }

    private fun createSenderName(message: StreamChatMessage): String {
        val result = StringBuilder()
        val sender = message.sender
        val senderName = sender.name
        val senderSurname = sender.surname

        if(senderName == null && senderSurname == null) {
            result.append(sender.id)
        } else {
            sender.name?.let(result::append)
            sender.surname?.let {
                if(result.isNotEmpty() && !result.endsWith(" ")) result.append(" ")
                result.append(it)
            }
        }

        return result.toString()
    }
}