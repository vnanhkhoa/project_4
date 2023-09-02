package com.khoavna.loacationreminders.di

import com.khoavna.loacationreminders.di.modules.appDataSource
import com.khoavna.loacationreminders.di.modules.appRepository
import com.khoavna.loacationreminders.di.modules.daoModule
import com.khoavna.loacationreminders.di.modules.useCaseModule
import org.koin.dsl.module

val appModule = module {
    includes(
        daoModule, appDataSource, appRepository, useCaseModule
    )
}