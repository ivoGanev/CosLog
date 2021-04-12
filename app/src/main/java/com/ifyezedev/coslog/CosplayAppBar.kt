package com.ifyezedev.coslog

import com.google.android.material.appbar.MaterialToolbar

class CosplayAppBar(private val appBar: MaterialToolbar) {

    fun displayShareButton() {
        appBar.hideOverflowMenu()
    }

}