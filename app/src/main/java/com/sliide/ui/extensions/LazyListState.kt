package com.sliide.ui.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow

@Composable
internal fun LazyListState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var index = 0
        var scroll = Int.MAX_VALUE

        snapshotFlow { firstVisibleItemIndex to firstVisibleItemScrollOffset }
            .collect { (current, currentScroll) ->
                if (current != index || currentScroll != scroll) {
                    value = current < index || (current == index && currentScroll < scroll)
                    index = current
                    scroll = currentScroll
                }
            }
    }
}
