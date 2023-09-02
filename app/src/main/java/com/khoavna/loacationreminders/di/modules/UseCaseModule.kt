package com.khoavna.loacationreminders.di.modules

import com.khoavna.loacationreminders.domain.location.LocationUseCase
import com.khoavna.loacationreminders.domain.location.LocationUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::LocationUseCaseImpl) { bind<LocationUseCase>() }
}