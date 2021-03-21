package com.loloof64.chessoexscanner

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.NoSuchElementException

sealed class Error
object EngineNotStarted : Error()
data class CannotStartEngine(val msg: String) : Error()
data class CannotCommunicateWithEngine(val msg: String) : Error()
data class MiscError(val msg: String) : Error()

class ChessEngineRunner(
    private val engineFile: File,
    val errorCallback: (Error) -> Unit
) {
    private val mutex = Mutex()
    var isRunning = AtomicBoolean(false)
    var isUCI = false
    var startedOk = AtomicBoolean(false)
    var mainJob: Job? = null
    var process: Process? = null
    var outputQueue = LinkedList<String>()
    var processBufferedReader: BufferedReader? = null
    var processOutputStream: OutputStream? = null

    fun run() = runBlocking {
        outputQueue.clear()
        // starter coroutine
        coroutineScope {
            launch {
                delay(10000)
                if (startedOk.get() && isRunning.get() && !isUCI) {
                    errorCallback(EngineNotStarted)
                }
            }
        }

        // running engine coroutine
        mainJob = GlobalScope.launch {
            try {
                val engineAbsolutePath = engineFile.absolutePath
                mutex.withLock {
                    process = ProcessBuilder(engineAbsolutePath).start()
                    if (process == null) {
                        errorCallback(CannotCommunicateWithEngine(""))
                        return@launch
                    }
                }
                while (isActive) {
                    val inputStream = process!!.inputStream
                    val inputSteamReader = InputStreamReader(inputStream)
                    processBufferedReader = BufferedReader(inputSteamReader, 8192)
                    processOutputStream = process!!.outputStream

                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            runCatching {
                                var first = true
                                var line: String? = null
                                mutex.withLock {
                                    line = processBufferedReader?.readLine()
                                }
                                while (line != null) {
                                    outputQueue.add(line!!)
                                    if (first) {
                                        startedOk.set(true)
                                        isRunning.set(true)
                                        first = false
                                    }
                                    mutex.withLock {
                                        line = processBufferedReader?.readLine()
                                    }
                                }
                            }
                        }
                    } catch (ignore: IOException) {
                    } catch (ex: Exception) {
                        errorCallback(MiscError(ex.message ?: ""))
                    }
                }
            } catch (ex: Exception) {
                when (ex) {
                    is SecurityException -> errorCallback(CannotStartEngine(ex.message ?: ""))
                    is IOException -> errorCallback(CannotCommunicateWithEngine(ex.message ?: ""))
                    else -> errorCallback(MiscError(ex.message ?: ""))
                }

            }
        }
    }

    fun sendCommand(command: String) {
        val carriageReturnTerminatedCmd = "$command\n"
        try {
            runBlocking {
                mutex.withLock {
                    if (process != null) {
                        processOutputStream?.write(carriageReturnTerminatedCmd.toByteArray())
                        processOutputStream?.flush()
                    }
                }
            }
        } catch (ignore: IOException) {
        }
    }

    fun readPendingOutputs(): Array<String> {
        val results = mutableListOf<String>()
        try {
            while (true) {
                runBlocking {
                    mutex.withLock {
                        val next = outputQueue.remove()
                        results.add(next)
                    }
                }
            }
        } catch (ex: NoSuchElementException) {

        } finally {
            return results.toTypedArray()
        }
    }

    fun stop() {
        mainJob?.cancel()
        processBufferedReader?.close()
        processOutputStream?.close()
    }
}