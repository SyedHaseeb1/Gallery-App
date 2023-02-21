package com.hsb.mygalleryapp.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hsb.mygalleryapp.Utils.rout
import com.hsb.mygalleryapp.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    private val STORAGE_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_PERMISSION_REQUEST_CODE = 200
    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if storage permission is granted
        if (isStoragePermissionGranted() && isCameraPermissionGranted()) {
            rout(MainActivity::class.java)
            finish()
        }

        binding.grantPermission.setOnClickListener {
            requestStoragePermission()
            requestCameraPermission()
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    private fun isCameraPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Storage permission granted, check if camera permission is also granted
                    if (isCameraPermissionGranted()) {
                        // Both permissions granted, start new activity
                        rout(MainActivity::class.java)
                        finish()
                    } else {
                        // Camera permission not granted, request it
                        requestCameraPermission()
                    }
                } else {
                    // Storage permission not granted, handle accordingly
                    // ...
                }
            }
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, check if storage permission is also granted
                    if (isStoragePermissionGranted()) {
                        // Both permissions granted, start new activity
                        rout(MainActivity::class.java)
                        finish()
                    } else {
                        // Storage permission not granted, request it
                        requestStoragePermission()
                    }
                } else {
                    // Camera permission not granted, handle accordingly
                    // ...
                }
            }
        }
    }

}
