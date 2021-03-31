package com.ifyezedev.coslog

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.navArgs
import com.ifyezedev.coslog.databinding.FragmentCosplayDetailsBinding

class CosplayDetailsFragment : BindingFragment<FragmentCosplayDetailsBinding>() {
    // Inflate the layout for this fragment
    override fun bindingLayoutId() = R.layout.fragment_cosplay_details
    private lateinit var source : From

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //access the arguments passed to fragment
        val args: CosplayDetailsFragmentArgs by navArgs()
        source = args.from

        setHasOptionsMenu(true)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cosplay_details_menu, menu)

        //hide certain menu items depending on the source
        if(source == From.BUTTON) {
            menu.findItem(R.id.delete_cosplay)?.isVisible = false
        }
        else {
            menu.findItem(R.id.cancel_adding)?.isVisible = false
        }

    }
}

