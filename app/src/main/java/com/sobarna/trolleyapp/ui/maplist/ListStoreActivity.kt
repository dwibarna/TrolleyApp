package com.sobarna.trolleyapp.ui.maplist

import android.Manifest
import android.content.Intent
import android.content.Intent.EXTRA_SUBJECT
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.sobarna.trolleyapp.R
import com.sobarna.trolleyapp.ui.detail.StoreDetailActivity
import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.databinding.ActivityListStoreBinding
import com.sobarna.trolleyapp.domain.model.Store
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class ListStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListStoreBinding
    private val viewModel: ListStoreViewModel by viewModels()
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted)
                getMyLocation()
            else
                finishAffinity()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun showProgressBar(state: Boolean) {
        state.let {
            with(binding){
                rvListItem.isVisible = !it
                progressBar.isVisible = it
            }
        }
    }

    private fun initAdapterList(location: Location) {
        viewModel.getListStore().observe(this) { result ->
            when (result) {
                is Resource.Error -> {
                    showProgressBar(false)
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> showProgressBar(true)
                is Resource.Success -> {
                    showProgressBar(false)
                    val mapAdapter = MapAdapter(location)
                    mapAdapter.submitList(result.data)
                    getMarker(result.data ?: emptyList())
                    binding.rvListItem.apply {
                        adapter = mapAdapter
                        layoutManager = LinearLayoutManager(context)
                    }
                    mapAdapter.setActionClick(actionClick = {
                        Intent(this@ListStoreActivity, StoreDetailActivity::class.java).apply {
                            putExtra(EXTRA_SUBJECT, it)
                        }.let(::startActivity)
                    })
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
        getMyLocation()
    }

    private fun getMarker(data: List<Store>) {
        data.forEach {
            val latLng = LatLng((it.latitude.toDoubleOrNull() ?: 0.0), (it.longitude.toDoubleOrNull() ?: 0) as Double)
            mMap.addMarker(
                MarkerOptions().apply {
                    position(latLng)
                    title(it.storeName)
                    snippet(
                        getAddressName(
                            lat = (it.latitude.toDoubleOrNull() ?: 0) as Double,
                            long = (it.longitude.toDoubleOrNull() ?: 0) as Double
                        )
                    )
                }
            )
            boundsBuilder.include(latLng)
        }

        val bounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    @Suppress("DEPRECATION")
    private fun getAddressName(lat: Double, long: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, long, 1)
            if (list != null && list.size != 0) {
                addressName = list.first().getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        initAdapterList(location)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
                    }
                }
        }
        else
            requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
    }
}