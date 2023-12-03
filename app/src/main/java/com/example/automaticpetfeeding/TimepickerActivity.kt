package com.example.automaticpetfeeding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.automaticpetfeeding.databinding.ActivityTimepickerBinding

class TimepickerActivity : AppCompatActivity() {

    lateinit var binding: ActivityTimepickerBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityTimepickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}