package com.demon.qfsolution.loader

import android.net.Uri
import android.widget.ImageView

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail idemon_liu@qq.com
 * Desc: 图片加载器接口，定义如何加载图片
 */
interface IQFImgLoader {

    /**
     * 图片显示在表格选择器中的缩略图
     * @param img ImageView
     * @param uri
     */
    fun displayThumbnail(img: ImageView, uri: Uri)


    /**
     * 一个Uri图片，使用大图预览时
     * @param img ImageView
     * @param uri 图片Uri
     */
    fun displayImgUri(img: ImageView, uri: Uri)


    /**
     * 一个图片Url或者图片路径，使用大图预览时
     * @param img ImageView
     * @param str 图片Url或者图片路径
     */
    fun displayImgString(img: ImageView, str: String)

}