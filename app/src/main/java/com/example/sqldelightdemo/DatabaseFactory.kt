package com.example.sqldelightdemo

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.sqldelightdemo.db.DemoDatabase

object DatabaseFactory {
    fun create(context: Context): DemoDatabase {
        val driver = AndroidSqliteDriver(DemoDatabase.Schema, context, "demo.db")
        return DemoDatabase(driver)
    }
}