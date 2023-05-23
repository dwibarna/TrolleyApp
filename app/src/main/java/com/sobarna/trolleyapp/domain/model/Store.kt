package com.sobarna.trolleyapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store(
    val id: Int,
    val storeId: String,
    val storeCode: String,
    val channelName: String,
    val areaName: String,
    val address: String,
    val dcName: String,
    val latitude: String,
    val regionId: String,
    val areaId: String,
    val accountId: String,
    val dcId: String,
    val subchannelId: String,
    val accountName: String,
    val storeName: String,
    val subchannelName: String,
    val regionName: String,
    val channelId: String,
    val longitude: String,
    val dateVisit: String = ""
): Parcelable