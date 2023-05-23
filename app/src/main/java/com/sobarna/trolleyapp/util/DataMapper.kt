package com.sobarna.trolleyapp.util

import com.sobarna.trolleyapp.data.source.local.entity.StoreEntity
import com.sobarna.trolleyapp.data.source.remote.response.LoginResponse
import com.sobarna.trolleyapp.domain.model.Store

object DataMapper {

    private fun mapperEntityToDomain(entity: StoreEntity): Store {
        with(entity) {
            return Store(
                id = id,
                storeId,
                storeCode,
                channelName,
                areaName,
                address,
                dcName,
                latitude,
                regionId,
                areaId,
                accountId,
                dcId,
                subchannelId,
                accountName,
                storeName,
                subchannelName,
                regionName,
                channelId,
                longitude,
                dateVisit
            )
        }
    }

    fun mapperListEntityToListDomain(entity: List<StoreEntity>): List<Store> {
        val list: MutableList<Store> = mutableListOf()
        entity.forEach {
            mapperEntityToDomain(it).let(list::add)
        }
        return list
    }

    fun mapperListDomainToEntity(store: List<Store>): List<StoreEntity> {
        val list: MutableList<StoreEntity> = mutableListOf()
        store.forEach {
            mapperDomainToEntity(it).let(list::add)
        }
        return list
    }

    fun mapperResponseToEntity(loginResponse: LoginResponse): List<StoreEntity> {
        val list: MutableList<StoreEntity> = mutableListOf()
        loginResponse.stores?.forEach {
            StoreEntity(
                id = 0,
                it.storeId ?: "",
                it.storeCode ?: "",
                it.channelName ?: "",
                it.areaName ?: "",
                it.address ?: "",
                it.dcName ?: "",
                it.latitude ?: "",
                it.regionId ?: "",
                it.areaId ?: "",
                it.accountId ?: "",
                it.dcId ?: "",
                it.subchannelId ?: "",
                it.accountName ?: "",
                it.storeName ?: "",
                it.subchannelName ?: "",
                it.regionName ?: "",
                it.channelId ?: "",
                it.longitude ?: "",
                ""
            ).let(list::add)
        }
        return list
    }

    fun mapperDomainToEntity(store: Store): StoreEntity {
        with(store) {
            return StoreEntity(
                id,
                storeId,
                storeCode,
                channelName,
                areaName,
                address,
                dcName,
                latitude,
                regionId,
                areaId,
                accountId,
                dcId,
                subchannelId,
                accountName,
                storeName,
                subchannelName,
                regionName,
                channelId,
                longitude,
                dateVisit
            )
        }
    }
}