package io.tryrook.rooknative.feature.login

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    init {
        println("LoginViewModel init-------------------------------------------------------------------")
    }

    override fun onCleared() {
        println("LoginViewModel clear------------------------------------------------------------------")

        super.onCleared()
    }
}
