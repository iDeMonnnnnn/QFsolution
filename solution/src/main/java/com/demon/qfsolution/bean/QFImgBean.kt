package com.demon.qfsolution.bean

import android.net.Uri

/**
 * @author DeMon
 * Created on 2020/11/3.
 * E-mail idemon_liu@qq.com
 * Desc:
 */
class QFImgBean {
    lateinit var uri: Uri
    var path: String = ""
    var type = 0  //0正常图片，1拍照选项
    var isSelected = false

    constructor(uri: Uri, path: String) {
        this.uri = uri
        this.path = path;
    }

    constructor(type: Int) {
        this.type = type
    }

    constructor(uri: Uri, isSelected: Boolean) {
        this.uri = uri
        this.isSelected = isSelected
    }


}