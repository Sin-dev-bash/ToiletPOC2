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
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_BLUETOOTH_SCAN = 1
    }

    private lateinit var bleManager: BLEMangaer
    private lateinit var scanResultTextView: TextView
    private lateinit var scanButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("MainActivity onCreate")

        bleManager = BLEMangaer()
        scanResultTextView = findViewById(R.id.scanResultTextView)
        scanButton = findViewById(R.id.scanButton)
        progressBar = findViewById(R.id.progressBar)

        scanButton.setOnClickListener {
            Timber.i("Scan button clicked")
            checkPermissionsAndScan()
        }
    }

    private fun checkPermissionsAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            scanBLEDevices()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_CODE_BLUETOOTH_SCAN)
        }
    }

    private fun scanBLEDevices() {
        progressBar.visibility = View.VISIBLE // スキャン開始時にプログレスバーを表示
        Timber.d("Starting BLE Scan")

        bleManager.startScan { address, rssi ->
            runOnUiThread {
                progressBar.visibility = View.GONE // スキャン結果が得られたらプログレスバーを非表示
                scanResultTextView.text = "Address: $address\nRSSI: $rssi"
                Timber.i("Scan Result - Address: $address, RSSI: $rssi")
            }
        }

        // スキャンを指定時間後に停止し、プログレスバーを非表示にする
        Handler(Looper.getMainLooper()).postDelayed({
            bleManager.stopScan()
            progressBar.visibility = View.GONE
        }, 4000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_BLUETOOTH_SCAN) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBLEDevices()
            } else {
                Timber.i("Bluetooth scan permission was denied by user.")
                // ユーザーにパーミッションが必要であることを通知
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("MainActivity onDestroy")
        // 必要に応じてリソースの解放や終了処理
    }
}
