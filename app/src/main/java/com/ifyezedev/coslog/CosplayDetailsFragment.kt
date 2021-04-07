package com.ifyezedev.coslog

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ifyezedev.coslog.databinding.FragmentCosplayDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class CosplayDetailsFragment : Fragment() {
    // Inflate the layout for this fragment
    lateinit var binding: FragmentCosplayDetailsBinding
    private lateinit var source : From
    private val PICK_IMAGE_CODE = 1
    lateinit var datePicker : MaterialDatePicker<Long>
    lateinit var viewModel: CosplayDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //access the arguments passed to fragment
        val args: CosplayDetailsFragmentArgs by navArgs()
        source = args.from

        //notify activity that this fragment has options menu
        setHasOptionsMenu(true)

        //setup binding
        binding = FragmentCosplayDetailsBinding.inflate(inflater)

        //setup viewmodel and binding lifecycleOwner
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(CosplayDetailsViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addCharImg.setOnClickListener { getImageIntent() }

        binding.removeCharImg.setOnClickListener { removeImage() }

        //initialize datePicker
        datePicker = viewModel.createDatePicker()
        //open datepicker when end icon is clicked
        binding.dueDateTextLayout.setEndIconOnClickListener { datePicker.show(requireActivity().supportFragmentManager, "Picker") }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        //hide delete menu option depending on the source
        if(source == From.BUTTON) {
            menu.findItem(R.id.delete_cosplay)?.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cosplay_details_menu, menu)
    }

    //intent for picking an image from the gallery
    private fun getImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_CODE)
    }

    //when the user selects an image, set it to imageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK){
            if (data != null){
                //get the image uri
                val imgUri = data.data

                //convert uri to bitmap
                val inputStream = imgUri?.let { requireContext().contentResolver.openInputStream(it) }
                val bitmap = BitmapFactory.decodeStream(inputStream)

                //set image to imageview
                binding.characterEditImg.setImageURI(imgUri)

            }
        }
    }

    //method to remove user selected image from imageView and database
    private fun removeImage() {
        //set the imageview to the placeholder image
        binding.characterEditImg.setImageResource(R.drawable.placeholder_image)
    }


}

