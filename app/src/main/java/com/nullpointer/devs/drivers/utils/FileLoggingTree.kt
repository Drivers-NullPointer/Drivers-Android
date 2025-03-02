package com.nullpointer.devs.drivers.utils

import android.util.Log
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class FileLoggingTree(
    private val logDir: File,
    private val maxFileSize: Long = 1024 * 1024,
    private val maxFiles: Int = 5
) : Timber.Tree() {

    private val logFile = File(logDir, "app.log")

    init {
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
        rotateLogsIfNeeded()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        try {
            val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val logMessage = "$timeStamp ${priorityToString(priority)}/$tag: $message\n"
            writeLogToFile(logMessage)
        } catch (e: Exception) {
            Timber.tag("FileLoggingTree").e(e, "Error writing log")
        }
    }

    private fun writeLogToFile(logMessage: String) {
        synchronized(this) {
            FileWriter(logFile, true).use { writer ->
                writer.append(logMessage)
            }
            rotateLogsIfNeeded()
        }
    }

    private fun rotateLogsIfNeeded() {
        if (logFile.length() > maxFileSize) {
            val archiveFile = File(logDir, "app-${System.currentTimeMillis()}.log")
            logFile.renameTo(archiveFile)
            logFile.createNewFile()

            cleanOldLogs()
        }
    }

    private fun cleanOldLogs() {
        val logFiles = logDir.listFiles { _, name -> name.startsWith("app-") && name.endsWith(".log") }
        logFiles?.sortedByDescending { it.lastModified() }
            ?.drop(maxFiles)
            ?.forEach { it.delete() }
    }

    private fun priorityToString(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }
    }
}
