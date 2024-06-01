package com.bangkit.nest.ui.main.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatalogViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is catalog Fragment"
    }
    val text: LiveData<String> = _text
}