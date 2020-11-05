package com.demon.qf_app

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.demon.qfsolution.R
import com.demon.qfsolution.loader.IQFImgLoader

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail 757454343@qq.com
 * Desc:
 */
class GlideLoader : IQFImgLoader {
    override fun displayThumbnail(img: ImageView, uri: Uri) {
        val options = RequestOptions().error(R.drawable.ic_qf_img).placeholder(R.drawable.ic_qf_img).centerCrop()
        //thumbnail缩略图
        Glide.with(img).asBitmap().thumbnail(0.5f).apply(options).load(uri).into(img)
    }

    override fun displayImgUri(img: ImageView, uri: Uri) {
        val options = RequestOptions().error(R.drawable.ic_qf_img).placeholder(R.drawable.ic_qf_img).override(Target.SIZE_ORIGINAL)
        Glide.with(img).asBitmap().apply(options).load(uri).into(img)
    }

    override fun displayImgString(img: ImageView, str: String) {
        val options = RequestOptions().error(R.drawable.ic_qf_img).placeholder(R.drawable.ic_qf_img).override(Target.SIZE_ORIGINAL)
        Glide.with(img).asBitmap().apply(options).load(str).into(img)
    }
}