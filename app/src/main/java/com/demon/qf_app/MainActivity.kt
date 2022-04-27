package com.demon.qf_app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.demon.qf_app.databinding.ActivityMainBinding
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.loader.QFImgLoader
import com.demon.qfsolution.utils.*
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.GlobalScope
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private var uri: Uri? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }

        binding.btn1.setOnClickListener {
            launchUI {
                uri = openFile()
                Log.i(TAG, "openFile: $uri")
                binding.ivImg.setImageURI(uri)
            }
        }

        binding.btn2.setOnClickListener {
            launchUI {
                uri = openPhotoAlbum()
                Log.i(TAG, "openPhotoAlbum: $uri")
                binding.ivImg.setImageURI(uri)
            }
        }
        binding.btn3.setOnClickListener {
            launchUI {
                val path = gotoCamera<String>("DeMon-${System.currentTimeMillis()}.jpg")
                Log.i(TAG, "gotoCamera: $path =")
                //binding.ivImg.setImageURI(uri)
                Glide.with(binding.ivImg).load(path).into(binding.ivImg)
            }
        }


        binding.btn4.setOnClickListener {
            launchUI {
                uri?.run {
                    uri = startCrop(this, 300, 300)
                    Log.i(TAG, "startCrop: $uri")
                    binding.ivImg.setImageURI(uri)
                }
            }
        }
        binding.btn5.setOnClickListener {
            launchUI {
                uri?.run {
                    uri = startCrop(this)
                    Log.i(TAG, "startCrop: $uri")
                    binding.ivImg.setImageURI(uri)
                }
            }
        }

        binding.btn6.setOnClickListener {
            startActivity(Intent(this, ImgBrowseActivity::class.java))
        }


    }


}