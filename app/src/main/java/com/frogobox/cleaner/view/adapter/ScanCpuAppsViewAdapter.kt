package com.frogobox.cleaner.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frogobox.cleaner.R
import com.frogobox.cleaner.model.Apps
import kotlinx.android.synthetic.main.list_cpu_cooler_doing.view.*

/**
 * Created by Frogobox Software Industries 2/25/2017.
 */

// Get List of Apps Causing Junk Files
class ScanCpuAppsViewAdapter(private val apps: List<Apps>) :
        RecyclerView.Adapter<ScanCpuAppsViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_cpu_cooler_doing, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (_, image) = apps[position]
        holder.image.setImageDrawable(image)
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.appimage
    }
}
