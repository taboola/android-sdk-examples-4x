package com.taboola.kotlin.examples.screens.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import com.taboola.kotlin.examples.R

class NativeComposeWidgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val taboolaNativeWrapperViewModel = TaboolaNativeWidgetWrapperViewModel()

        setContent {
            //Add TBLClassicUnit to the UI (to layout)
            Column() {
                Text(text = resources.getString(R.string.lorem_ipsum))
                ArticleTitle(taboolaNativeWrapperViewModel)
                ArticleImage(taboolaNativeWrapperViewModel)
            }
        }
        //Fetching recommendations
        taboolaNativeWrapperViewModel.fetchRecommendation(requireContext())
    }

    @Composable
    fun ArticleImage(viewModelWidget: TaboolaNativeWidgetWrapperViewModel) {
        viewModelWidget.getTBLImage()?.let {
            AndroidView(factory = { _ ->
                it
            }, modifier = Modifier.fillMaxWidth())
        }
    }

    @Composable
    fun ArticleTitle(viewModelWidget: TaboolaNativeWidgetWrapperViewModel) {
        viewModelWidget.getTBLTitle()?.let {
            AndroidView(factory = { _ -> it })
        }
    }
}