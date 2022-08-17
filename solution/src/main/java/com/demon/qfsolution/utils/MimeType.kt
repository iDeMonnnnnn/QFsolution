package com.demon.qfsolution.utils

/**
 * @author DeMon
 * Created on 2020/10/23.
 * E-mail idemon_liu@qq.com
 * Desc: Android常用MimeType，参考：https://www.w3school.com.cn/media/media_mimeref.asp
 */
object MimeType {
    const val all = "*/*"
    const val img = "image/*"
    const val video = "video/*"
    const val audio = "audio/*"
    const val text = "text/*"
    const val apk = "application/vnd.android.package-archive"
    const val zip = "application/x-zip-compressed"
    const val pdf = "application/pdf"

    /**
     * Word
     */
    const val doc = "application/msword"
    const val docx = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

    /**
     * PPT
     */
    const val ppt = "application/vnd.ms-powerpoint"
    const val pptx = "application/vnd.openxmlformats-officedocument.presentationml.presentation"

    /**
     * Excel
     */
    const val xls = "application/vnd.ms-excel"
    const val xlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    const val _png = "image/png"
    const val _jpeg = "image/jpeg"
    const val _jpg = "image/jpeg"
    const val _webp = "image/webp"
    const val _gif = "image/gif"
    const val _bmp = "image/bmp"
    const val _3gp = "video/3gpp"
    const val _asf = "video/x-ms-asf"
    const val _avi = "video/x-msvideo"
    const val _bin = "application/octet-stream"
    const val _c = "text/plain"
    const val _class = "application/octet-stream"
    const val _conf = "text/plain"
    const val _cpp = "text/plain"
    const val _exe = "application/octet-stream"
    const val _gtar = "application/x-gtar"
    const val _gz = "application/x-gzip"
    const val _h = "text/plain"
    const val _htm = "text/html"
    const val _html = "text/html"
    const val _jar = "application/java-archive"
    const val _java = "text/plain"
    const val _js = "application/x-javascript"
    const val _log = "text/plain"
    const val _m3u = "audio/x-mpegurl"
    const val _m4a = "audio/mp4a-latm"
    const val _m4b = "audio/mp4a-latm"
    const val _m4p = "audio/mp4a-latm"
    const val _m4u = "video/vnd.mpegurl"
    const val _m4v = "video/x-m4v"
    const val _mov = "video/quicktime"
    const val _mp2 = "audio/x-mpeg"
    const val _mp3 = "audio/x-mpeg"
    const val _mp4 = "video/mp4"
    const val _mpc = "application/vnd.mpohun.certificate"
    const val _mpe = "video/mpeg"
    const val _mpeg = "video/mpeg"
    const val _mpg = "video/mpeg"
    const val _mpg4 = "video/mp4"
    const val _mpga = "audio/mpeg"
    const val _msg = "application/vnd.ms-outlook"
    const val _ogg = "audio/ogg"
    const val _pps = "application/vnd.ms-powerpoint"
    const val _prop = "text/plain"
    const val _rc = "text/plain"
    const val _rmvb = "audio/x-pn-realaudio"
    const val _rtf = "application/rtf"
    const val _sh = "text/plain"
    const val _tar = "application/x-tar"
    const val _tgz = "application/x-compressed"
    const val _txt = "text/plain"
    const val _wav = "audio/x-wav"
    const val _wma = "audio/x-ms-wma"
    const val _wmv = "audio/x-ms-wmv"
    const val _wps = "application/vnd.ms-works"
    const val _xml = "text/plain"
    const val _z = "application/x-compress"

}

fun String.isWord() = this == MimeType.doc || this == MimeType.docx

fun String.isPPT() = this == MimeType.ppt || this == MimeType.pptx

fun String.isExcel() = this == MimeType.xls || this == MimeType.xlsx

fun String.isImage() = this.startsWith("image/")

fun String.isGif() = this == MimeType._gif

fun String.isVideo() = this.startsWith("video/")

fun String.isAudio() = this.startsWith("audio/")

fun String.isTxt() = this.startsWith("text/")

fun String.isPdf() = this == MimeType.pdf

fun String.isApk() = this == MimeType.apk

fun String.isZip() = this == MimeType.zip
        || this == MimeType._tar
        || this == MimeType._tgz
        || this == MimeType._z
        || this == MimeType._gtar
        || this == MimeType._gz


