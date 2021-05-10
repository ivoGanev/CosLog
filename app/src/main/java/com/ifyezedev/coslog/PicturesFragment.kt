package com.ifyezedev.coslog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.ifyezedev.coslog.core.common.usecase.OpenAndroidImageGallery
import com.ifyezedev.coslog.data.db.CosLogDatabase
import com.ifyezedev.coslog.feature.elements.details.ElementsDetailsViewModel

class PicturesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }


}