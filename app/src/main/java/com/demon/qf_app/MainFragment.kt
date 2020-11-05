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
import androidx.fragment.app.Fragment
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.utils.*
import kotlinx.android.synthetic.main.activity_main.*
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

    var uri: Uri? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_main, container, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn1.setOnClickListener {
            GlobalScope.launchUI {
                uri = openFile<String>(arrayListOf(MimeType.img))?.run {
                    File(this).toUri()
                }
                Log.i(TAG, "onCreate: $uri")
                img.setImageURI(uri)
            }
        }
        btn2.setOnClickListener {
            GlobalScope.launchUI {
                uri = gotoCamera(fileName = "DeMon-${System.currentTimeMillis()}.jpg")
                Log.i(TAG, "onCreate: $uri")
                img.setImageURI(uri)
            }
        }
        btn3.setOnClickListener {
            QFHelper.getInstance()
                .isNeedGif(false)
                .isNeedCamera(true)
                .setSpanCount(3)
                .setLoadNum(30)
                .setMaxNum(9)
                .start(this, 0x001)
        }

        btn4.setOnClickListener {
            GlobalScope.launchUI {
                uri?.run {
                    uri = startCrop(this, 300, 600)
                    img.setImageURI(uri)
                }
            }
        }

        btn5.visibility = View.GONE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                0x001 -> {
                    val uris = QFHelper.getInstance().getResult(data)
                    uris?.run {
                        uri = this[0]
                        img.setImageURI(uri)
                    }
                }
            }
            //Log.i(TAG, "onActivityResult: $requestCode  $uri  ${uri.uriToFile(this)}")
            //img.setImageURI(uri)
        }
    }
}