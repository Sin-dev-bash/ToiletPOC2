package com.example.toiletpoc

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import timber.log.Timber

class BLEMangaer {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var scanCallback: ScanCallback? = null

    fun startScan(onDeviceFound: (String, Int) -> Unit) {
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let { device ->
                    onDeviceFound(device.address, result.rssi)
                    Timber.d("Device found: ${device.address} with RSSI: ${result.rssi}")
                }
            }
        }

        bluetoothAdapter.bluetoothLeScanner.startScan(scanCallback)
        Timber.d("Starting BLE Scan")
    }

    fun stopScan() {
        scanCallback?.let {
            bluetoothAdapter.bluetoothLeScanner.stopScan(it)
            Timber.d("Stopped BLE Scan")
        }
    }
}
