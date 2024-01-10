package com.example.toiletpoc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toiletpoc.R

class ScanResultAdapter(private val scanResults: MutableList<Pair<String, Int>>) :
    RecyclerView.Adapter<ScanResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceAddress: TextView = view.findViewById(R.id.deviceAddress)
        val deviceRssi: TextView = view.findViewById(R.id.deviceRssi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (address, rssi) = scanResults[position]
        holder.deviceAddress.text = address
        holder.deviceRssi.text = "RSSI: $rssi"
    }

    override fun getItemCount() = scanResults.size

    fun addDevice(result: Pair<String, Int>) {
        if (!scanResults.contains(result)) {
            scanResults.add(result)
            notifyDataSetChanged()
        }
    }
}
