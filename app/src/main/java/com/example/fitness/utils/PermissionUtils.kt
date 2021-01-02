package com.example.fitness.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity


object PermissionUtils { // проверка разрешений от пользователя

    private val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    const val LOCATION_REQUEST_CODE = 101

    fun requestLocationPermission(activity: AppCompatActivity): Boolean {
        if (checkLocationPermission(activity))                                  //если true функция возвращает true и код дальше не выполняется
            return true

        activity.requestPermissions(locationPermission, LOCATION_REQUEST_CODE)  //если false вывод диалог
        return false
    }

    private fun checkLocationPermission(activity: AppCompatActivity) =  // проверка есть ли разрешение есть true, иначе false
        activity.checkSelfPermission(locationPermission[0]) == PackageManager.PERMISSION_GRANTED
}