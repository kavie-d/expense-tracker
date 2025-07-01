package com.example.expensetracker.config

import android.app.Application

class InitApplicationContainer: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}