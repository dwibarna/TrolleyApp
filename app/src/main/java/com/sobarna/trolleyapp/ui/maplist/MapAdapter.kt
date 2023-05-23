package com.sobarna.trolleyapp.ui.maplist

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sobarna.trolleyapp.databinding.ContentMapListBinding
import com.sobarna.trolleyapp.domain.model.Store

class MapAdapter(private val currentLocation: Location): ListAdapter<Store, MapAdapter.ViewHolder>(
    DiffCallBack
) {

    private var actionClick: ((Store) -> Unit)? = null

    fun setActionClick(actionClick: ((Store) -> Unit)? = null) {
        this.actionClick = actionClick
    }

    inner class ViewHolder(private val binding: ContentMapListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(store: Store) {
            with(binding) {
                tvAddress.text = store.address
                tvAreaName.text = store.storeName
                tvDcName.text = store.areaName

                itemView.setOnClickListener {
                    actionClick?.invoke(store)
                }

                val destinationLocation = Location("Destination")
                destinationLocation.latitude = store.latitude.toDoubleOrNull() ?: 0.0
                destinationLocation.longitude = store.longitude.toDoubleOrNull() ?: 0.0
                calculateDistance(currentLocation, destinationLocation).let {
                    tvRange.text = it
                }
                store.dateVisit.isNotBlank().let {
                    ivStateVisit.isVisible = it
                }
            }
        }
    }

    private fun calculateDistance(currentLocation: Location, destinationLocation: Location): String {
        val distanceInMeters = currentLocation.distanceTo(destinationLocation)

        return when {
            distanceInMeters < 1000 -> "${distanceInMeters.toInt()} m"
            distanceInMeters > 20000 -> "Out of location"
            else -> "${(distanceInMeters / 1000).toInt()} km"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ContentMapListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bindItem(it)
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem.id == newItem.id
        }

    }
}