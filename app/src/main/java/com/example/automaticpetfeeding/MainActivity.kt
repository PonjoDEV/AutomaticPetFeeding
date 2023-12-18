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
import com.example.automaticpetfeeding.databinding.ActivityMainBinding
import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.widget.ArrayAdapter
import android.widget.Toast
import java.io.IOException
import java.util.UUID

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var binding:ActivityMainBinding
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothSocket: BluetoothSocket
    lateinit var bluetoothItens: MutableList<String>
    lateinit var adapter:ArrayAdapter<String>
    lateinit var bluetRun:Runnable
    private val REQUEST_LOCATION_PERM = 99

    var HC05on:Boolean = false

    //MACadress of the HC-05 Module on Arduino
    var macAddress:String = "00:21:13:00:8C:22"

    lateinit var btAdapter: BluetoothAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        startUp()
        //Automatically searching for the devices once the program boots up
        searchDevices()

    }
    private fun startUp() {
        binding.feedButton.setOnClickListener(this)
        binding.timePicker.setOnClickListener(this)
        binding.profilePicker.setOnClickListener(this)
        binding.searchDevices.setOnClickListener(this)
        binding.refreshHC05.setOnClickListener(this)
    }

    //Adding actions to the buttons on the Screen
    override fun onClick(bt: View) {
        when (bt.id) {
            binding.feedButton.id -> {
                //Sends a Y indicating a "YES" for manual activation
                feedNow("Y")
                //Then it sends a blank space to stop
                feedNow(" ")
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
            binding.refreshHC05.id -> {
                refreshHC05()
            }

        }
    }

    //Button call
    private fun refreshHC05() {

    }

    //Button call
    @SuppressLint("MissingPermission")
    private fun searchDevices() {
        bluetoothItens = mutableListOf()
        adapter = ArrayAdapter(baseContext, R.layout.simple_list_item_1,bluetoothItens)

        //binding.feedbackTxt.adapter = adapter

        bluetoothConfig()
        connectByBlueTooth(macAddress)
    }

    //Button call
    private fun feedNow(command:String) {
        try {
            val outputStream = bluetoothSocket.outputStream
            outputStream.write(command.toByteArray())
        }catch (e: IOException){
            Toast.makeText(this,"Não foi possível alimentar agora, reconectar ao HC05",Toast.LENGTH_LONG).show()
        }
    }

    //Button call Transitioning to the profile table layout
    private fun profileList() {
        val transitionProfileList: Intent = Intent(baseContext, ProfileActivity::class.java)
        startActivity(transitionProfileList)
    }

    //Button callTransitioning to the time of feeding Schedule layout
    private fun feedingSchedule() {
        val transitionFeedingSchedule: Intent = Intent(baseContext, TimepickerActivity::class.java)
        startActivity(transitionFeedingSchedule)
    }

    //Conecting to the HC05
    @SuppressLint("MissingPermission")
    private fun connectByBlueTooth(deviceAddress: String) {

        bluetRun = Runnable {

            val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // UUID para comunicação SPP

            try {
                // Criação de um soquete Bluetooth para comunicação
                val socket: BluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)

                // Tenta conectar ao dispositivo
                socket.connect()
                bluetoothSocket = socket // Armazena o soquete para uso posterior

                runOnUiThread {
                    Toast.makeText(this, "Conectado ao HC-05", Toast.LENGTH_SHORT).show()
                    // Chama a função feedNow após a conexão bem-sucedida
                }

            } catch (e: IOException) {
                // Lida com exceções de conexões falhas
                runOnUiThread {
                    Toast.makeText(this, "Erro na conexão Bluetooth", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
        bluetRun.run()
    }

    //Setting bluetooth configuration
    private fun bluetoothConfig() {
        //Checking for permissions
        requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,  Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_PRIVILEGED),0)

        bluetoothManager = baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.getAdapter()

        //Checking if bluetooth is on
        if (!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT) !=
                PackageManager.PERMISSION_GRANTED) {
                return
            }
            startActivity(enableBluetoothIntent)
        }

        //If the BT isnt conected it pops up the option to enable it and calls the function again
        val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, discoverDevicesIntent)

        bluetoothAdapter.startDiscovery()
    }

    private val receiver = object : BroadcastReceiver() {
        //Verificação realizada em outra função
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)!!
                bluetoothItens.add(device.address)
                adapter.notifyDataSetChanged()
            }
        }
    }

    //Remover o registro ao dispositivo conectado com bluetooth
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}