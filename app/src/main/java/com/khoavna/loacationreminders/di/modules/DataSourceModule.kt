package com.khoavna.loacationreminders.di.modules

import com.khoavna.loacationreminders.data.datasource.location.LocationDataSource
import com.khoavna.loacationreminders.data.datasource.location.LocationDataSourceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appDataSource = module {
    singleOf(::LocationDataSourceImpl) { bind<LocationDataSource>() }
}