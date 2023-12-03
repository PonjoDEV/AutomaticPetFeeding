package com.example.automaticpetfeeding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.example.automaticpetfeeding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        startUp()
    }

    private fun startUp() {
        binding.feedButton.setOnClickListener(this)
    }

    //Adding actions to the buttons on the Screen
    override fun onClick(bt: View) {
        when (bt.id) {
            binding.feedButton.id -> {

            }
        }
    }
}