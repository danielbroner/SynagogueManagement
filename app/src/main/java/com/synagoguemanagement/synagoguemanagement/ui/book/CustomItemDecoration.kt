package com.synagoguemanagement.synagoguemanagement.ui.book

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // Get item position
        val spanCount = 9 // Grid layout columns

        // Check if it's in the 3rd row
        if (position in (2 * spanCount) until (3 * spanCount)) {
            outRect.top = spacing
            outRect.bottom = spacing
        }
    }
}