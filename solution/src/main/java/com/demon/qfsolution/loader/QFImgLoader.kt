package com.demon.qfsolution.loader

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.NonNull

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail idemon_liu@qq.com
 * Desc:
 */
class QFImgLoader : IQFImgLoader {
    private var loader: IQFImgLoader? = null

    fun init(@NonNull loader: IQFImgLoader) {
        this.loader = loader
    }

    override fun displayThumbnail(img: ImageView, uri: Uri) {
        loader?.displayThumbnail(img, uri)
    }

    override fun displayImgString(img: ImageView, str: String) {
        loader?.displayImgString(img, str)
    }

    override fun displayImgUri(img: ImageView, uri: Uri) {
        loader?.displayImgUri(img, uri)
    }

    companion object {

        @Volatile
        private var instance: QFImgLoader? = null

        @JvmStatic
        fun getInstance(): QFImgLoader {
            return instance ?: synchronized(this) {
                instance ?: QFImgLoader().also { instance = it }
            }
        }
    }
}