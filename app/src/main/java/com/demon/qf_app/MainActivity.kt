package com.demon.qf_app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.demon.qf_app.databinding.ActivityMainBinding
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.loader.QFImgLoader
import com.demon.qfsolution.utils.*
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.GlobalScope
import java.io.File

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    var uri: Uri? = null
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
                uri = openFile<String>(arrayListOf(MimeType.img))?.run {
                    File(this).toUri()
                }
                Log.i(TAG, "onCreate: $uri")
                binding.img.setImageURI(uri)
            }
        }
        binding.btn2.setOnClickListener {
            launchUI {
                uri = gotoCamera(fileName = "DeMon-${System.currentTimeMillis()}.jpg")
                Log.i(TAG, "onCreate: $uri")
                binding.img.setImageURI(uri)
            }
        }
        binding.btn3.setOnClickListener {
            QFHelper
                .isNeedGif(false)
                .isNeedCamera(true)
                .setSpanCount(3)
                .setLoadNum(30)
                .setMaxNum(9)
                .start(this, 0x001)
        }

        binding.btn31.setOnClickListener {
            launchUI {
                val uris = QFHelper
                    .isNeedGif(false)
                    .isNeedCamera(true)
                    .setSpanCount(3)
                    .setLoadNum(30)
                    .setMaxNum(9)
                    .startScopeUri(this)

                Log.i(TAG, "onCreate: startScopeUri=$uris")
            }
        }

        binding.btn4.setOnClickListener {
            launchUI {
                uri?.run {
                    uri = startCrop(this, 300, 300)
                    Log.i(TAG, "startCrop: $uri")
                    binding.img.setImageURI(uri)
                }
            }
        }
        binding.btn5.setOnClickListener {
            launchUI {
                uri?.run {
                    uri = startCrop(this)
                    Log.i(TAG, "startCrop: $uri")
                    binding.img.setImageURI(uri)
                }
            }
        }
        binding.btn6.setOnClickListener {
            var fragment = supportFragmentManager.findFragmentByTag(MainFragment::class.java.simpleName)
            if (fragment == null) {
                fragment = MainFragment()
                supportFragmentManager.beginTransaction().add(fragment, MainFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0x001 -> {
                    val uris = QFHelper.getResult(data)
                    uris?.run {
                        uri = this[0]
                        Log.i(TAG, "onActivityResult: $uri")
                        binding.img.setImageURI(uri)
                    }
                }
            }
        }
    }

}