package com.tde.frameworkDemo

import android.app.Application
import com.tde.framework.base.BaseViewModel
import com.tde.framework.base.model.SpaceModel

class SetViewModel(application: Application, val testParams: String) : BaseViewModel<SpaceModel>(application) {


}