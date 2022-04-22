package com.demon.qf_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.demon.qf_app.databinding.ActivityMainBinding
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.utils.*
import kotlinx.coroutines.GlobalScope
import java.io.File

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail 757454343@qq.com
 * Desc:
 */
class MainFragment : DialogFragment() {

    private val TAG = "MainFragment"
    private lateinit var binding: ActivityMainBinding
    var uri: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    uri = startCrop(this, 300, 600)
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
        binding.btn6.visibility = View.GONE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                0x001 -> {
                    val uris = QFHelper.getResult(data)
                    uris?.run {
                        uri = this[0]
                        binding.img.setImageURI(uri)
                    }
                }
            }
            //Log.i(TAG, "onActivityResult: $requestCode  $uri  ${uri.uriToFile(this)}")
            //img.setImageURI(uri)
        }
    }
}