package com.example.toiletpoc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_BLUETOOTH_SCAN = 1
        private const val REQUEST_CODE_BLUETOOTH_CONNECT = 2
        private const val REQUEST_CODE_LOCATION = 3
    }

    private lateinit var bleManager: BLEMangaer
    private lateinit var scanResultsRecyclerView: RecyclerView
    private lateinit var scanButton: Button
    private lateinit var progressBar: ProgressBar
    private val bleDeviceAdapter = BLEDeviceAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bleManager = BLEMangaer()
        scanResultsRecyclerView = findViewById(R.id.scanResultsRecyclerView)
        scanResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        scanResultsRecyclerView.adapter = bleDeviceAdapter

        scanButton = findViewById(R.id.scanButton)
        progressBar = findViewById(R.id.progressBar)

        scanButton.setOnClickListener {
            Timber.i("Scan button clicked")
            checkPermissionsAndScan()
        }
    }

    private fun checkPermissionsAndScan() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_CODE_BLUETOOTH_SCAN)

            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH_CONNECT)

            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ->
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)

            else -> scanBLEDevices()
        }
    }


    private fun scanBLEDevices() {
        progressBar.visibility = View.VISIBLE
        Timber.d("Starting BLE Scan")

        bleManager.startScan { device ->
            runOnUiThread {
                val bleDevice = BLEDevice(
                    name = device.name ?: "Unknown", // デバイス名がnullの場合は"Unknown"
                    rssi = device.rssi,
                    address = device.address,
                    isConnected = false // 実際の接続状態を反映する必要があります
                )
                bleDeviceAdapter.addDevice(bleDevice)
            }
        }


            Handler(Looper.getMainLooper()).postDelayed({
                bleManager.stopScan()
                progressBar.visibility = View.GONE // プログレスバーを非表示
            }, 4000)
        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_CODE_BLUETOOTH_SCAN || requestCode == REQUEST_CODE_LOCATION) &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermissionsAndScan()
        } else {
            Timber.i("Required permission was denied by user.")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("MainActivity onDestroy")
    }
}
