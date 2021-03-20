package com.loloof64.chessoexscannerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.loloof64.chessoexscannerdemo.databinding.ActivityMainBinding
import com.loloof64.chessoexscanner.ChessEngineUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainView.adapter = CustomAdapter(ChessEngineUtils.getEnginesNames(context = this))
        binding.mainView.layoutManager = LinearLayoutManager(this)
    }
}