package com.khoavna.loacationreminders.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.khoavna.loacationreminders.ui.model.FirebaseUserLiveData

class LoginViewModel : ViewModel() {

    private val _firebaseUser = FirebaseUserLiveData()
    val isLogin: LiveData<Boolean> = _firebaseUser.map { it == null }
}