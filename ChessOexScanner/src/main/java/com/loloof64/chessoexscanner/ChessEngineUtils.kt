package com.loloof64.chessoexscanner

import android.content.Context
import com.kalab.chess.enginesupport.ChessEngineResolver
import java.io.File

data class EngineData(val fileName: String, val packageName: String, val versionCode: Int)

object ChessEngineUtils {
    private const val enginesSubfolderName = "engines"

    fun getEnginesNames(context: Context): Array<String> {
        val resolver = ChessEngineResolver(context)
        val engines = resolver.resolveEngines()
        return engines?.map { it.name }?.toTypedArray() ?: arrayOf()
    }

    fun getEnginesCount(context: Context): Int {
        val resolver = ChessEngineResolver(context)
        val engines = resolver.resolveEngines()
        return engines?.size ?: 0
    }

    fun copyEngineInInternalFiles(context: Context, index: Int): EngineData {
        val resolver = ChessEngineResolver(context)
        val engines = resolver.resolveEngines()
        if (index > engines.size) throw ArrayIndexOutOfBoundsException("Requested index : ${index}, size: ${engines.size}")

        val selectedEngine = engines[index]
        val engineFileName = selectedEngine.fileName
        val enginePackageName = selectedEngine.packageName
        val engineVersionCode = selectedEngine.versionCode

        selectedEngine.copyToFiles(
            context.applicationContext.contentResolver,
            File(context.applicationContext.filesDir, enginesSubfolderName)
        )

        return EngineData(
            fileName = engineFileName,
            packageName = enginePackageName,
            versionCode = engineVersionCode
        )
    }

    fun getEngineLastVersionCode(context: Context, currentData: EngineData): Int {
        val resolver = ChessEngineResolver(context)

        return resolver.ensureEngineVersion(
            currentData.fileName,
            currentData.packageName,
            currentData.versionCode,
            File(context.applicationContext.filesDir, enginesSubfolderName)
        )
    }
}