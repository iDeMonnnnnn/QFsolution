package com.demon.qf_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.demon.qf_app.databinding.ActivityImgBrowseBinding
import com.demon.qfsolution.QFHelper

class ImgBrowseActivity : AppCompatActivity() {
    private val TAG = "ImgBrowseActivity"
    private lateinit var binding: ActivityImgBrowseBinding

    private val adapter by lazy {
        ImgAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImgBrowseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvImgs.adapter = adapter

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
                //.startScopePath(this)
                uris?.run {
                    Log.i(TAG, " startScopeUri=$this")
                    adapter.datas = this.toMutableList()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                0x001 -> {
                    QFHelper.getResult(data)?.run {
                        adapter.datas = this.toMutableList()
                    }
                }
            }
        }
    }
}