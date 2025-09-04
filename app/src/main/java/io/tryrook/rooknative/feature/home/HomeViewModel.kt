package io.tryrook.rooknative.feature.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    init {
        println("HomeViewModel init-------------------------------------------------------------------")
    }

    override fun onCleared() {
        println("HomeViewModel clear------------------------------------------------------------------")

        super.onCleared()
    }
}
