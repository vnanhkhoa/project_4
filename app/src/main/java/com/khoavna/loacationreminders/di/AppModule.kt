package com.khoavna.loacationreminders.di

import com.khoavna.loacationreminders.di.modules.daoModule
import org.koin.dsl.module

val appModule = module {
    includes(
        daoModule
    )
}