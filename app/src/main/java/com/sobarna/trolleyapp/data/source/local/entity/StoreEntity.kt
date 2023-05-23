package com.sobarna.trolleyapp.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store")
data class StoreEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo("store_id") val storeId: String,

    @ColumnInfo("store_code") val storeCode: String,

    @ColumnInfo("channel_name") val channelName: String,

    @ColumnInfo("area_name") val areaName: String,

    @ColumnInfo("address") val address: String,

    @ColumnInfo("dc_name") val dcName: String,

    @ColumnInfo("latitude") val latitude: String,

    @ColumnInfo("region_id") val regionId: String,

    @ColumnInfo("area_id") val areaId: String,

    @ColumnInfo("account_id") val accountId: String,

    @ColumnInfo("dc_id") val dcId: String,

    @ColumnInfo("subchannel_id") val subchannelId: String,

    @ColumnInfo("account_name") val accountName: String,

    @ColumnInfo("store_name") val storeName: String,

    @ColumnInfo("subchannel_name") val subchannelName: String,

    @ColumnInfo("region_name") val regionName: String,

    @ColumnInfo("channel_id") val channelId: String,

    @ColumnInfo("longitude") val longitude: String,

    @ColumnInfo("dateVisit") val dateVisit: String
)