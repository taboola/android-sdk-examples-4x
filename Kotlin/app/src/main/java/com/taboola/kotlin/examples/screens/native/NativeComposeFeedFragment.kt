package com.taboola.kotlin.examples.screens.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.taboola.android.tblnative.TBLImageView
import com.taboola.android.tblnative.TBLTextView
import kotlinx.coroutines.flow.collect

/**
 * To implement a Taboola Feed in "Native Integration" we use a Compose LazyColumn to layout incoming items.
 */
class NativeComposeFeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val taboolaNativeFeedWrapperViewModel = TaboolaNativeFeedWrapperViewModel()

        setContent {
            //Add TBLClassicUnit to the UI (to layout)
            val listState = rememberLazyListState()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), state = listState) {
                items(taboolaNativeFeedWrapperViewModel.getArticles()) { article ->
                    Column() {
                        ArticleTitle(tblTextView = article.title)
                        ArticleImage(tblImageView = article.image)
                    }
                }
            }
            listState.OnBottomReached(positionsNumberBuffer = 2) {
                taboolaNativeFeedWrapperViewModel.fetchRecommendation(requireContext());
            }
        }
    }

    @Composable
    fun ArticleImage(tblImageView: TBLImageView?) {
        tblImageView?.let {
            AndroidView(factory = { _ ->
                it
            }, modifier = Modifier.fillMaxWidth())
        }
    }

    @Composable
    fun ArticleTitle(tblTextView: TBLTextView?) {
        tblTextView?.let {
            AndroidView(factory = { _ -> it })
        }
    }

    @Composable
    fun LazyListState.OnBottomReached(
        positionsNumberBuffer: Int = 0,
        loadMore: () -> Unit
    ) {

        require(positionsNumberBuffer >= 0) { "buffer cannot be negative, but was $positionsNumberBuffer" }

        val shouldLoadMore = remember {
            derivedStateOf {
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf true
                // checking if we reached the position which should invoke load more items
                // if the positionsNumberBuffer is 0 it means when reaching the last position load more should be called
                // if you want to call should load more before reaching the last position provide value to positionsNumberBuffer
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - positionsNumberBuffer
            }
        }

        //  Convert the state into a cold flow and collect
        LaunchedEffect(shouldLoadMore) {
            snapshotFlow { shouldLoadMore.value }.collect() {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
        }
    }


}

