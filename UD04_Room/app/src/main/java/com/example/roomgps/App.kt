package com.example.roomgps

import android.app.Application
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                Log.e("App", "Uncaught exception", throwable)
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                throwable.printStackTrace(pw)
                val trace = sw.toString()

                val file = File(filesDir, "crash_log.txt")
                file.appendText("\n--- Crash at: ${System.currentTimeMillis()} ---\n")
                file.appendText(trace)
            } catch (e: Exception) {
                Log.e("App", "Error saving crash log", e)
            }

            // Rethrow the exception to let the system handle the crash as usual
            try {
                Thread.sleep(500)
            } catch (_: InterruptedException) {
            }
            // Optionally kill the process
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(2)
        }
    }
}
