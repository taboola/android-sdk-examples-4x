package com.taboola.kotlin.examples.screens.native

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.taboola.android.tblnative.TBLRecommendationItem
import com.taboola.kotlin.examples.R

class NativeFeedAdapter(data: List<Any>) : RecyclerView.Adapter<ViewHolder>() {
    private var mData: List<Any>? = null

    init {
        mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val linearLayout = inflater.inflate(R.layout.feed_taboola_item, parent, false) as LinearLayout
        return TaboolaItemViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mData == null) {
            println("Data is null.")
            return
        }

        val item = mData!![position] as TBLRecommendationItem
        val adContainer = (holder as TaboolaItemViewHolder).mAdContainer
        val context = adContainer.context
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val thumbnailView = item.getThumbnailView(context)
        val titleView = item.getTitleView(context)
        val brandingView = item.getBrandingView(context)
        val descriptionView = item.getDescriptionView(context)
        adContainer.addView(thumbnailView)
        adContainer.addView(titleView, layoutParams)
        if (brandingView != null) {
            adContainer.addView(brandingView, layoutParams)
        }
        if (descriptionView != null) {
            adContainer.addView(descriptionView, layoutParams)
        }
    }


    override fun getItemCount(): Int {
        if (mData == null) {
            return 0
        }

        return mData!!.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is TaboolaItemViewHolder) {
            holder.mAdContainer.removeAllViews()
        }
    }

    internal class TaboolaItemViewHolder(var mAdContainer: LinearLayout) : ViewHolder(mAdContainer)
}