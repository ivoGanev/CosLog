package com.ifyezedev.coslog

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

// TODO: The class name is ambiguous and it implies that is somehow related to
//        CosplayActivity, but its not.
class CosplayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //let android know this fragment has a menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cosplay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //go to CosplayDetailsFragment to add a cosplay
        view.findViewById<FloatingActionButton>(R.id.addCosplayBttn).setOnClickListener {
            it.findNavController().navigate(CosplayFragmentDirections.actionCosplayFragmentToCosplayDetailsFragment(
                From.BUTTON))
        }
    }



    //inflate menu resource
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    //override this so menu items open their respective fragments
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}