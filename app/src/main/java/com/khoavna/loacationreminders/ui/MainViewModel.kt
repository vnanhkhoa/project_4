package com.khoavna.loacationreminders.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.khoavna.loacationreminders.ui.dto.FirebaseUserLiveData

class MainViewModel : ViewModel() {

    private val _firebaseUser = FirebaseUserLiveData()
    val isLogin: LiveData<Boolean> = _firebaseUser.map { it != null }
}