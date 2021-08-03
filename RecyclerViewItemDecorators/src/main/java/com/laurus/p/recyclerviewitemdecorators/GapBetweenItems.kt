package com.laurus.p.recyclerviewitemdecorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Works with LinearLayoutManager
 * @gap is in pixels
 */
class GapBetweenItems(private val gap: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = (parent.layoutManager as? LinearLayoutManager) ?: return
        val orientation = layoutManager.orientation
        val isReverseLayout = layoutManager.reverseLayout
        val offset = getOffsetForElement(parent, view)

        when(orientation){
            RecyclerView.HORIZONTAL -> {
                if(!isReverseLayout){
                    outRect.left = offset
                } else{
                    outRect.right = offset
                }
            }
            else -> {
                if(!isReverseLayout){
                    outRect.top = offset
                } else{
                    outRect.bottom = offset
                }
            }
        }
    }

    private fun getOffsetForElement(
        recyclerView: RecyclerView,
        view: View
    ): Int{
        return when (recyclerView.getChildAdapterPosition(view)) {
            RecyclerView.NO_POSITION,
            0 -> 0
            else -> gap
        }
    }
}