package com.demon.qf_app

import java.io.File
import java.io.FileReader

/**
 * @author DeMon
 * Created on 2023/12/5.
 * E-mail demonl@binarywalk.com
 * Desc:
 */
object FileUtils {
    /**
     * 读取文件中的字符串
     *
     * @param fileName
     * @return
     */
    @JvmStatic
    fun readText(fileName: String): String {
        if (!File(fileName).exists()) return ""
        val sb = StringBuilder()
        try {
            val fr = FileReader(fileName)
            val buf = CharArray(1024)
            var num = 0
            while (fr.read(buf).also { num = it } != -1) {
                sb.append(String(buf, 0, num))
            }
            fr.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}