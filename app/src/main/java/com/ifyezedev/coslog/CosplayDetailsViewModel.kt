package com.ifyezedev.coslog

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ifyezedev.coslog.data.db.CosLogDao
import com.ifyezedev.coslog.data.db.entities.Cosplay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CosplayDetailsViewModel(val database: CosLogDao) : ViewModel() {

    //livedata that stores the date selected from date picker
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

    //livedata that stores the current date
    private val _initialDate = MutableLiveData<String>()
    val initialDate: LiveData<String>
        get() = _initialDate

    //livedata that stores the error to be displayed to the user
    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    //global variables for long initial and due date
    private var initialLongDate: Long = Calendar.getInstance().timeInMillis
    private var dueLongDate: Long? = null


    //date formatter
    private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())


    init {
        //set initial date to live data
        _initialDate.value = getInitialDate()
    }

    //function to get today's date
    private fun getInitialDate() : String {
        return initialLongDate.toFormattedDate(dateFormatter)
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
                dueLongDate = longDate
                _selectedDate.value = longDate.toFormattedDate(dateFormatter)
            }

        }

        return picker
    }


    //save the cosplay to database
    fun saveCosplay(characterName: String, series: String, budget: String) {

        //if name or series are null or empty, warn user
        if (characterName.trim().isEmpty() or  series.trim().isEmpty()) {
            _error.value = "Character name and series must be provided"
            return
        }


        //if name or series isn't empty save cosplay to database
        val budgetDouble = budget.toDoubleOrNull()
        val currentTime = initialLongDate

        val cosplay = Cosplay(0,
            characterName,
            series,
            initialLongDate,
            dueLongDate,
            currentTime,
            budgetDouble)

        Log.i("viewmodel cosplay obj", cosplay.toString())

        viewModelScope.launch(Dispatchers.IO) {
            database.insertCosplay(cosplay)
        }
    }


}