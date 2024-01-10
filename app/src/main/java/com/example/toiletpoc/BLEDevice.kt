package com.example.toiletpoc

data class BLEDevice(    val name: String?,
                         val rssi: Int,
                         val address: String,
                         val isConnected: Boolean // 仮の接続状態フラグ
)
