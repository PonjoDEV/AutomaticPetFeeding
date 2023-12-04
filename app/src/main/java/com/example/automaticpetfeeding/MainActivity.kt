package com.example.automaticpetfeeding

import android.content.Intent
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
        binding.timePicker.setOnClickListener(this)
        binding.profilePicker.setOnClickListener(this)
    }

    //Adding actions to the buttons on the Screen
    override fun onClick(bt: View) {
        when (bt.id) {
            binding.feedButton.id -> {

            }
            binding.timePicker.id -> {
                feedingSchedule()
            }
            binding.profilePicker.id -> {
                profileList()
            }
        }
    }

    private fun profileList() {
        val transitionProfileList: Intent = Intent(baseContext, ProfileActivity::class.java)
        startActivity(transitionProfileList)
    }

    //Transitioning to the time of feeding Schedule layout
    private fun feedingSchedule() {
        val transitionFeedingSchedule: Intent = Intent(baseContext, TimepickerActivity::class.java)
        startActivity(transitionFeedingSchedule)
    }
}