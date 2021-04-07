package com.ifyezedev.coslog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class CosplayDetailsViewModel : ViewModel() {
    //livedata that stores the date selected from date picker
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    //livedata that stores the current date
    private val _initialDate = MutableLiveData<String>()
    val initialDate: LiveData<String>
        get() = _initialDate

    //date formatter
    val format = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())

    init {
        _initialDate.value = getInitialDate()
    }

    //get today's date
    fun getInitialDate() : String {
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = Date()
        return format.format(date)
    }

    //datePicker for setting due date
    fun createDatePicker(): MaterialDatePicker<Long> {

        // Makes only dates from today forward selectable.
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select due date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        picker.addOnPositiveButtonClickListener {
            val longDate = picker.selection
            if (longDate != null){
                format.timeZone = TimeZone.getTimeZone("UTC")
                val date = Date(longDate)
                _selectedDate.value = format.format(date)
            }

        }

        return picker
    }

    fun saveImage(ImgBitmap : Bitmap) {
        //todo: save image to internal storage, save image location info to database (move this todo to method that will when user clicks save)
    }

    fun deleteImage() {
        //todo: remove image from database (when loading cosplay details from database if image is null display placeholder)
    }

}