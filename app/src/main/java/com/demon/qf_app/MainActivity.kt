package com.demon.qf_app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
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

    private val openDocumentTree = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        it ?: return@registerForActivityResult
        Log.i(TAG, "openDocumentTree: ${it.uriToFile()?.absolutePath}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        }
        PermissionX.init(this)
            .permissions(*permissions)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "需要文件存储&管理权限，相机权限", "好的", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }

        binding.btn1.setOnClickListener {
            launchUI {
                val path = openFile<String>() ?: ""
                Log.i(TAG, "openFile: $path")
                //Glide.with(binding.ivImg).load(path).into(binding.ivImg)
                //binding.ivImg.setImageURI(uri)

                FileUtils.readText(path)
                //path.saveToAlbum()
            }
        }

        binding.btn11.setOnClickListener {
            openDocumentTree.launch(null)
        }

        binding.btn2.setOnClickListener {
            launchUI {
                val path = openPhotoAlbum<String>()
                uri = File(path).getFileUri()
                Log.i(TAG, "openPhotoAlbum: $uri")
                binding.ivImg.setImageURI(uri)
            }
        }
        binding.btn3.setOnClickListener {
            launchUI {
                val file = gotoCamera<File>("DeMon-${System.currentTimeMillis()}.jpg")
                Log.i(TAG, "gotoCamera: ${file?.absolutePath} =")
                uri = file?.getFileUri()
                binding.ivImg.setImageURI(uri)
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