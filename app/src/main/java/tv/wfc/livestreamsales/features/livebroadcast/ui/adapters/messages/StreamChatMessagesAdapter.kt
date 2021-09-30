package tv.wfc.livestreamsales.features.livebroadcast.ui.adapters.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import tv.wfc.livestreamsales.R
import tv.wfc.livestreamsales.application.model.streamchatmessage.StreamChatMessage

class StreamChatMessagesAdapter(
    streamChatMessagesDiffUtilItemCallback: DiffUtil.ItemCallback<StreamChatMessage>
): ListAdapter<StreamChatMessage, StreamChatMessageViewHolder>(streamChatMessagesDiffUtilItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamChatMessageViewHolder {
        val messageView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)

        return StreamChatMessageViewHolder(messageView)
    }

    override fun onBindViewHolder(holder: StreamChatMessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}