package com.example.automaticpetfeeding

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.automaticpetfeeding.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.widget.Toast
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding:ActivityMainBinding
    var HC05on:Boolean = false
    lateinit var macAddress:String

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val PERMISSION_REQUEST_BLUETOOTH = 2
    }

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

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
        binding.searchDevices.setOnClickListener(this)
        binding.conetcHC05.setOnClickListener(this)
    }

    //Adding actions to the buttons on the Screen
    override fun onClick(bt: View) {
        when (bt.id) {
            binding.feedButton.id -> {
                feedNow()
            }
            binding.timePicker.id -> {
                feedingSchedule()
            }
            binding.profilePicker.id -> {
                profileList()
            }
            binding.searchDevices.id -> {
                searchDevices()
            }
            binding.conetcHC05.id -> {
                conectHC05()
            }

        }
    }

    private fun conectHC05() {

    }

    private fun searchDevices() {
        TODO("Not yet implemented")
    }


    private fun feedNow():Boolean {
        if (HC05on) {
            //TODO
        }else{
            println("HC-05 não conectado")
        }
        return HC05on
    }

    //Transitioning to the profile table layout
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