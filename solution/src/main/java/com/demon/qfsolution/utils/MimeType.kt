package com.demon.qfsolution.utils

/**
 * @author DeMon
 * Created on 2020/10/23.
 * E-mail 757454343@qq.com
 * Desc: Android常用MimeType，参考：https://www.w3school.com.cn/media/media_mimeref.asp
 */
enum class MimeType(val value: String) {
    all("*/*"),
    img("image/*"),
    video("video/*"),
    audio("audio/*"),
    text("text/*"),
    apk("application/vnd.android.package-archive"),
    zip("application/x-zip-compressed"),
    pdf("application/pdf"),
    /**
     * Word
     */
    doc("application/msword"),
    docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    /**
     * PPT
     */
    ppt("application/vnd.ms-powerpoint"),
    pptx("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    /**
     * Excel
     */
    xls("application/vnd.ms-excel"),
    xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    _png("image/png"),
    _jpeg("image/jpeg"),
    _jpg("image/jpeg"),
    _webp("image/webp"),
    _gif("image/gif"),
    _bmp("image/bmp"),
    _3gp("video/3gpp"),
    _asf("video/x-ms-asf"),
    _avi("video/x-msvideo"),
    _bin("application/octet-stream"),
    _c("text/plain"),
    _class("application/octet-stream"),
    _conf("text/plain"),
    _cpp("text/plain"),
    _exe("application/octet-stream"),
    _gtar("application/x-gtar"),
    _gz("application/x-gzip"),
    _h("text/plain"),
    _htm("text/html"),
    _html("text/html"),
    _jar("application/java-archive"),
    _java("text/plain"),
    _js("application/x-javascript"),
    _log("text/plain"),
    _m3u("audio/x-mpegurl"),
    _m4a("audio/mp4a-latm"),
    _m4b("audio/mp4a-latm"),
    _m4p("audio/mp4a-latm"),
    _m4u("video/vnd.mpegurl"),
    _m4v("video/x-m4v"),
    _mov("video/quicktime"),
    _mp2("audio/x-mpeg"),
    _mp3("audio/x-mpeg"),
    _mp4("video/mp4"),
    _mpc("application/vnd.mpohun.certificate"),
    _mpe("video/mpeg"),
    _mpeg("video/mpeg"),
    _mpg("video/mpeg"),
    _mpg4("video/mp4"),
    _mpga("audio/mpeg"),
    _msg("application/vnd.ms-outlook"),
    _ogg("audio/ogg"),
    _pps("application/vnd.ms-powerpoint"),
    _prop("text/plain"),
    _rc("text/plain"),
    _rmvb("audio/x-pn-realaudio"),
    _rtf("application/rtf"),
    _sh("text/plain"),
    _tar("application/x-tar"),
    _tgz("application/x-compressed"),
    _txt("text/plain"),
    _wav("audio/x-wav"),
    _wma("audio/x-ms-wma"),
    _wmv("audio/x-ms-wmv"),
    _wps("application/vnd.ms-works"),
    _xml("text/plain"),
    _z("application/x-compress"),
    ;
}
