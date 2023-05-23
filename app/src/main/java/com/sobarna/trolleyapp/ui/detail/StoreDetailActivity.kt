package com.sobarna.trolleyapp.ui.detail

import android.content.Intent
import android.content.Intent.EXTRA_SUBJECT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sobarna.trolleyapp.R
import com.sobarna.trolleyapp.databinding.ActivityStoreDetailBinding
import com.sobarna.trolleyapp.domain.model.Store
import com.sobarna.trolleyapp.util.Utils.displayImageBitMap
import com.sobarna.trolleyapp.util.Utils.displayImageDrawable
import com.sobarna.trolleyapp.util.Utils.setCurrentDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoreDetailBinding
    private var store: Store? = null
    private var stateCamera = false
    private var stateLocation = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: StoreDetailViewModel by viewModels()

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            stateCamera = true
            initVisitAction()
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                displayImageBitMap(applicationContext, it, binding.ivPreview)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        store = intent.getParcelableExtra(EXTRA_SUBJECT)

        binding.ivTakePicture.setOnClickListener {
            if (!allPermissionCameraGranted())
                permissionUsageCamera()
            else
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                launcherCamera.launch(it)
            }
        }
        binding.ivLocation.setOnClickListener {
            getCurrentLocation()
        }

        initVisitAction()
        fetchIntentExtra()
        getCurrentLocation()
    }

    private fun initVisitAction() {
        displayImageDrawable(
            context = applicationContext, drawable = if (stateCamera) R.drawable.baseline_done_24
            else R.drawable.baseline_close_24, imageView = binding.ivCheckPicture
        )

        displayImageDrawable(
            context = applicationContext, drawable = if (stateLocation) R.drawable.baseline_done_24
            else R.drawable.baseline_close_24, imageView = binding.ivCheckLocation
        )

        if (stateCamera && stateLocation) {
            binding.btnVisit.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.bg_btn_login)
            binding.btnVisit.setOnClickListener {
                store?.let {
                    viewModel.updateStore(
                        Store(
                            it.id,
                            it.storeId,
                            it.storeCode,
                            it.channelName,
                            it.areaName,
                            it.address,
                            it.dcName,
                            it.latitude,
                            it.regionId,
                            it.areaId,
                            it.accountId,
                            it.dcId,
                            it.subchannelId,
                            it.accountName,
                            it.storeName,
                            it.subchannelName,
                            it.regionName,
                            it.channelId,
                            it.longitude,
                            dateVisit = setCurrentDate()
                        )
                    )
                }
                finish()
            }
        } else {
            binding.btnVisit.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.bg_btn_not_active)
        }
    }

    private fun fetchIntentExtra() {

        with(binding) {
            tvAddress.text = store?.address
            tvAreaName.text = store?.areaName
            tvDcName.text = store?.dcName
            tvRegionName.text = store?.regionName
            tvStoreName.text = store?.storeName
            tvStoreId.text = store?.storeId

            llVerified.isVisible = store?.dateVisit.isNullOrBlank()
            tvDateVisit.text = store?.dateVisit
        }
    }

    private fun permissionUsageCamera() {
        if (!allPermissionCameraGranted()) ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSION_CAMERA, REQUEST_CODE_PERMISSIONS_CAMERA
        )
    }

    private fun allPermissionCameraGranted() = REQUIRED_PERMISSION_CAMERA.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_PERMISSIONS_LOCATION
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    initVisitAction()
                    calculateDistance(location)
                }
            }
    }


    private fun calculateDistance(currentLocation: Location) {

        val destinationLocation = Location("dummyProvider").apply {
            latitude = store?.latitude?.toDoubleOrNull() ?: 0.0
            longitude = store?.longitude?.toDoubleOrNull() ?: 0.0
        }
        val distanceInMeters = currentLocation.distanceTo(destinationLocation)

        stateLocation = distanceInMeters < 1000 && distanceInMeters.toInt() <= 100
        initVisitAction()
    }


    companion object {
        private val REQUIRED_PERMISSION_CAMERA = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS_CAMERA = 99
        private const val REQUEST_CODE_PERMISSIONS_LOCATION = 29
    }
}