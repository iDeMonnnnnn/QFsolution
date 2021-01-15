package com.demon.qfsolution.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.R
import com.demon.qfsolution.loader.QFImgLoader
import com.demon.qfsolution.photoview.PhotoView

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail 757454343@qq.com
 * Desc: 大图预览
 */
class QFBigImgActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qf_big_img)
        val qf_photo_view = findViewById<PhotoView>(R.id.qf_photo_view)
        val uri = intent.getParcelableExtra<Uri>(QFHelper.EXTRA_IMG)
        if (uri == null) {
            val url = intent.getStringExtra(QFHelper.EXTRA_IMG)
            url?.let { QFImgLoader.getInstance().displayImgString(qf_photo_view, it) }
        } else {
            QFImgLoader.getInstance().displayImgUri(qf_photo_view, uri)
        }

    }


}