package com.code.book

import android.app.Application

class BookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: android.content.Context
            private set
    }
} 