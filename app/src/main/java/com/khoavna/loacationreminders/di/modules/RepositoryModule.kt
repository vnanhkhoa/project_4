package com.khoavna.loacationreminders.di.modules

import com.khoavna.loacationreminders.data.repository.location.LocationRepository
import com.khoavna.loacationreminders.data.repository.location.LocationRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appRepository = module {
    singleOf(::LocationRepositoryImpl) {
        bind<LocationRepository>()
    }
}