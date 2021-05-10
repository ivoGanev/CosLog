package com.ifyezedev.coslog.feature.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PictureGalleryPagerAdapter(
    fragment: Fragment,
    private val data: MutableList<String>,
) : FragmentStateAdapter(fragment) {

    fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int): String {
        return data[position]
    }

    fun getSize(): Int {
        return data.size
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return data[position].hashCode().toLong()
    }

    override fun createFragment(position: Int): Fragment {
        // println("Creating fragment: $position with path ${data[position].first}")
        val fragment = PictureGalleryItemFragment()
        fragment.arguments = Bundle().apply {
            putString(PictureGalleryFragment.Keys.IMAGE_PATH, data[position])
        }
        return fragment
    }
}