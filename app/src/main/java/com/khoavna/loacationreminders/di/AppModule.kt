package com.khoavna.loacationreminders.di

import com.khoavna.loacationreminders.di.modules.useCaseModule
import com.khoavna.loacationreminders.ui.locationdetail.LocationDetailViewModel
import com.khoavna.loacationreminders.ui.locations.LocationListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val locationListModule = module {
    includes(useCaseModule)
    viewModelOf(::LocationListViewModel)
}

val locationDetailModule = module {
    includes(useCaseModule)
    viewModelOf(::LocationDetailViewModel)
}

val appModule = listOf(
    locationListModule, locationDetailModule
)
