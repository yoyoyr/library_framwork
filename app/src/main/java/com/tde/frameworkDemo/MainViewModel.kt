package com.tde.frameworkDemo

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import com.tde.framework.base.BaseViewModel
import com.tde.framework.base.model.SpaceModel

class MainViewModel(application: Application) : BaseViewModel<SpaceModel>(application) {

    val text = ObservableField("去设置页")

    override fun onCreate() {
        super.onCreate()
        Log.d("zhuanghongzhan", "(MainViewModel.kt:10)onCreate-->")
    }


    override fun onPause() {
        super.onPause()
        Log.d("zhuanghongzhan", "(MainViewModel.kt:17)onPause-->")
    }

}