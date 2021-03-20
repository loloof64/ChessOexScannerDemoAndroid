package com.loloof64.chessoexscannerdemo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.loloof64.chessoexscannerdemo.databinding.ActivityMainBinding
import com.kalab.chess.enginesupport.ChessEngineResolver

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainView.adapter = CustomAdapter(getEnginesList(context = this))
        binding.mainView.layoutManager = LinearLayoutManager(this)
    }

    private fun getEnginesList(context: Context) : Array<String> {
        val resolver = ChessEngineResolver(context)
        val engines = resolver.resolveEngines()
        return engines?.map { it.name }?.toTypedArray() ?: arrayOf()
    }
}