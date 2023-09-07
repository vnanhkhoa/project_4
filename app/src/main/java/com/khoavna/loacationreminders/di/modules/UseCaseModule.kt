package com.khoavna.loacationreminders.di.modules

import com.khoavna.loacationreminders.domain.location.LocationUseCase
import com.khoavna.loacationreminders.domain.location.LocationUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    includes(appRepository)

    factoryOf(::LocationUseCaseImpl) { bind<LocationUseCase>() }
}