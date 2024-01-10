package com.example.toiletpoc

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import timber.log.Timber

class BLEMangaer {
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var scanCallback: ScanCallback? = null

    fun startScan(onDeviceFound: (BLEDevice) -> Unit) {
        scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let { device ->
                    val bleDevice = BLEDevice(
                        name = device.name ?: "Unknown",
                        rssi = result.rssi,
                        address = device.address,
                        isConnected = false // 仮の値、実際の接続状態を反映する必要がある
                    )
                    onDeviceFound(bleDevice)
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
