package com.ifyezedev.coslog

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar

class CosplayToolbarController(private val viewFlipper: ViewFlipper) {

    @IdRes
    private val imageGalleryId = R.id.appBarImageGallery

    @IdRes
    private val cosplayId = R.id.appBarCosplay

    private val pictureGalleryAppBarView = viewFlipper.findViewById<View>(imageGalleryId)

    private val cosplayAppBarView = viewFlipper.findViewById<View>(cosplayId)

    sealed class ToolbarType {
        object PictureGallery : ToolbarType() {
            override fun create(view: View): NavigationToolbar =
                PictureGalleryToolbar(view)
        }

        object Cosplay : ToolbarType() {
            override fun create(view: View): NavigationToolbar =
               CosplayToolbar(view)
        }

        abstract fun create(view: View): NavigationToolbar
    }

    private val toolbars = hashMapOf(
        ToolbarType.Cosplay to ToolbarType.Cosplay.create(cosplayAppBarView),
        ToolbarType.PictureGallery to ToolbarType.PictureGallery.create(pictureGalleryAppBarView),
    )

    var currentlyDisplayedToolbar: NavigationToolbar = toolbars[ToolbarType.Cosplay]!!

    fun getToolbar(toolBarType: ToolbarType) : NavigationToolbar {
        return toolbars[toolBarType]!!
    }

    fun displayAppBar(toolbarType: ToolbarType) {
        currentlyDisplayedToolbar = toolbars[toolbarType]!!
        viewFlipper.displayedChild = viewFlipper.indexOfChild(currentlyDisplayedToolbar.rootView)
    }
}

abstract class NavigationToolbar(val rootView: View) {
    fun setBackButtonListener(listener: View.OnClickListener) {
        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener(listener)
    }
}

class PictureGalleryToolbar(rootView: View) : NavigationToolbar(rootView) {
    fun setShareButtonListener(listener: View.OnClickListener) {
        val shareButton = rootView.findViewById<Button>(R.id.shareButton)
        shareButton.setOnClickListener(listener)
    }

    fun setTitle(text: String) {
        val header = rootView.findViewById<TextView>(R.id.header)
        header.text = text
    }
}

class CosplayToolbar(rootView: View) : NavigationToolbar(rootView) {
}