package com.taboola.kotlin.examples.screens.classic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.taboola.kotlin.examples.R

private const val NUM_PAGES = 2

/**
 * This code sample shows Taboola Units in a ViewPager.
 */
class ViewPagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_classic_viewpager, container, false)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = root.findViewById(R.id.viewpager)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        return root
    }

    /**
     * A simple pager adapter. Notice, for this Sample code it just shows 2 of the same page.
     */
    private inner class ScreenSlidePagerAdapter(hostFragment: Fragment) : FragmentStateAdapter(hostFragment) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ProgrammaticWidgetFragment()
                1 -> FeedFragment()
                else -> XmlWidgetFragment()
            }
        }
    }

}