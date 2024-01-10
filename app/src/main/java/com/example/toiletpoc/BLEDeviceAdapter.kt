package com.example.toiletpoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BLEDeviceAdapter(private val scanResults: MutableList<BLEDevice>) :
RecyclerView.Adapter<BLEDeviceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView = view.findViewById(R.id.deviceName)
        val deviceAddress: TextView = view.findViewById(R.id.deviceAddress)
        val deviceRssi: TextView = view.findViewById(R.id.deviceRssi)
        val deviceStatus: TextView = view.findViewById(R.id.deviceStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = scanResults[position]
        holder.deviceName.text = device.name ?: "Unknown Device"
        holder.deviceAddress.text = device.address
        holder.deviceRssi.text = "RSSI: ${device.rssi}"
        holder.deviceStatus.text = if (device.isConnected) "Connected" else "Not connected"
    }

    override fun getItemCount() = scanResults.size

    fun addDevice(device: BLEDevice) {
        // 既にリストに存在するデバイスをチェックする
        val existingDevice = scanResults.find { it.address == device.address }
        if (existingDevice == null) {
            scanResults.add(device)
            notifyDataSetChanged()
        } else {
            // 必要に応じて既存のデバイス情報を更新する
        }
    }
}
