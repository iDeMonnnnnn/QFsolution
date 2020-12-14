package com.demon.qfsolution.utils

import android.content.*
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.demon.qfsolution.fragment.GhostFragment
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.*
import java.net.URLConnection


/**
 * @author DeMon
 * Created on 2020/10/23.
 * E-mail 757454343@qq.com
 * Desc:
 */

/**
 * Activity中使用相机拍照
 * 根据泛型类型返回结果：
 * Uri：图片的Uri
 * File：图片的文件对象
 * String：图片的绝对路径
 *
 * @param isSave 是否保存至相册，默认true
 * @param fileName 拍照后的文件名，默认为空 取时间戳.jpg
 */
suspend inline fun <reified T : Any> FragmentActivity.gotoCamera(isSave: Boolean = true, fileName: String? = null): T? {
    return suspendCancellableCoroutine { continuation ->
        runCatching {
            val name = fileName ?: "${System.currentTimeMillis()}.jpg"
            val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File(getExternalFilesDir(Environment.DIRECTORY_DCIM), name)
            } else {
                File("${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_DCIM}", name)
            }
            val uri = file.getFileUri(this)
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val fm = supportFragmentManager
            val fragment = GhostFragment()
            fragment.init(intentCamera) {
                if (isSave) file.saveToAlbum(this)
                when (T::class.java) {
                    File::class.java -> {
                        continuation.resumeWith(Result.success(file as T))
                    }
                    Uri::class.java -> {
                        continuation.resumeWith(Result.success(uri as T))
                    }
                    String::class.java -> {
                        continuation.resumeWith(Result.success(file.absolutePath as T))
                    }
                    else -> {
                        Log.e("FileExt", "gotoCamera:Result only support File,Uri,String!")
                        continuation.resumeWith(Result.success(null))
                    }
                }
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
            fm.beginTransaction().add(fragment, GhostFragment::class.java.simpleName).commitAllowingStateLoss()
        }.onFailure {
            it.printStackTrace()
            continuation.resumeWith(Result.success(null))
        }
    }
}

/**
 * Fragment中使用相机拍照
 * @param isSave 是否保存至相册，默认true
 * @param fileName 文件名，默认为空取时间戳
 */
suspend inline fun <reified T : Any> Fragment.gotoCamera(isSave: Boolean = true, fileName: String? = null): T? {
    val activity = requireActivity()
    return activity.gotoCamera<T>(isSave, fileName)
}

/**
 * Activity中使用打开文件选择
 * 根据泛型类型返回结果：
 * Uri：文件的Uri
 * File：文件的File对象
 * String：文件的绝对路径
 * @param mimeTypes 文件[MimeType]，默认全部MimeType.all。也可多种类型如图片&文本：arrayListOf(MimeType.img, MimeType.text)
 */
suspend inline fun <reified T : Any> FragmentActivity.openFile(mimeTypes: List<MimeType> = arrayListOf(MimeType.all)): T? {
    return suspendCancellableCoroutine { continuation ->
        runCatching {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            val arrays = arrayOfNulls<String>(mimeTypes.size)
            for (i in mimeTypes.indices) {
                arrays[i] = mimeTypes[i].value
            }
            if (arrays.isNotEmpty()) intent.putExtra(Intent.EXTRA_MIME_TYPES, arrays)
            val fm = supportFragmentManager
            val fragment = GhostFragment()
            fragment.init(intent) {
                val uri = it?.data
                when (T::class.java) {
                    File::class.java -> {
                        val file = uri?.uriToFile(this)
                        continuation.resumeWith(Result.success(file as T))
                    }
                    Uri::class.java -> {
                        continuation.resumeWith(Result.success(uri as T))
                    }
                    String::class.java -> {
                        val file = uri?.uriToFile(this)
                        continuation.resumeWith(Result.success(file?.absolutePath as T))
                    }
                    else -> {
                        Log.e("FileExt", "openFile: Result only support File,Uri,String!")
                        continuation.resumeWith(Result.success(null))
                    }
                }
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
            fm.beginTransaction().add(fragment, GhostFragment::class.java.simpleName).commitAllowingStateLoss()
        }.onFailure {
            it.printStackTrace()
            continuation.resumeWith(Result.success(null))
        }
    }
}

/**
 * Fragment中使用打开文件选择
 * @param mimeTypes 文件[MimeType]，默认全部MimeType.all。也可多种类型如图片&文本：arrayListOf(MimeType.img, MimeType.text)
 */
suspend inline fun <reified T : Any> Fragment.openFile(mimeTypes: List<MimeType> = arrayListOf(MimeType.all)): T? {
    val activity = requireActivity()
    return activity.openFile<T>(mimeTypes)
}

/**
 * Activity中使用原生裁剪
 * @param uri content://URI格式的Uri文件
 * @param width 宽，用于计算裁剪比例
 * @param height 高，用于计算裁剪比例
 * @param isSave 是否保存至相册，默认true
 * @param fileName 裁剪后的文件名，默认为空 取时间戳.png
 */
suspend inline fun <reified T : Any> FragmentActivity.startCrop(uri: Uri, width: Int = 500, height: Int = 500, isSave: Boolean = true, fileName: String? = null): T? {
    return suspendCancellableCoroutine { continuation ->
        runCatching {
            val name = fileName ?: "${System.currentTimeMillis()}.png"
            val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), name)
            } else {
                File("${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_PICTURES}", name)
            }
            val crop_uri = Uri.fromFile(file)
            val intentCrop = Intent("com.android.camera.action.CROP")
            intentCrop.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intentCrop.setDataAndType(uri, "image/*")
            //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intentCrop.putExtra("crop", "true")
            //裁剪时是否保留图片的比例
            intentCrop.putExtra("scale", true)
            // aspectX aspectY 是宽高的比例
            intentCrop.putExtra("aspectX", width)
            intentCrop.putExtra("aspectY", height)
            // outputX outputY 是裁剪图片宽高
            intentCrop.putExtra("outputX", width)
            intentCrop.putExtra("outputY", height)
            //是否将数据保留在Bitmap中返回
            intentCrop.putExtra("return-data", false)
            //关闭人脸识别
            intentCrop.putExtra("noFaceDetection", true)
            //设置输出的格式
            intentCrop.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
            intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, crop_uri)
            val fm = supportFragmentManager
            val fragment = GhostFragment()
            fragment.init(intentCrop) {
                if (isSave) file.saveToAlbum(this)
                when (T::class.java) {
                    File::class.java -> {
                        continuation.resumeWith(Result.success(file as T))
                    }
                    Uri::class.java -> {
                        continuation.resumeWith(Result.success(crop_uri as T))
                    }
                    String::class.java -> {
                        continuation.resumeWith(Result.success(file.absolutePath as T))
                    }
                    else -> {
                        Log.e("FileExt", "gotoCamera:Result only support File,Uri,String!")
                        continuation.resumeWith(Result.success(null))
                    }
                }
                fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
            }
            fm.beginTransaction().add(fragment, GhostFragment::class.java.simpleName).commitAllowingStateLoss()
        }.onFailure {
            it.printStackTrace()
            continuation.resumeWith(Result.success(null))
        }
    }
}

suspend inline fun <reified T : Any> Fragment.startCrop(uri: Uri, width: Int = 500, height: Int = 500, isSave: Boolean = true, fileName: String? = null): T? {
    val activity = requireActivity()
    return activity.startCrop<T>(uri, width, height, isSave, fileName)
}


/**
 * 将Uri转为File
 */
fun Uri?.uriToFile(context: Context): File? {
    this ?: return null
    Log.i("FileExt", "uriToFile: $this")
    return when (scheme) {
        ContentResolver.SCHEME_FILE -> {
            File(this.path)
        }
        ContentResolver.SCHEME_CONTENT -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                getFileFromUriQ(context)
            } else {
                getFileFromUriN(context)
            }
        }
        else -> {
            File(toString())
        }
    }
}

/**
 * fileProvider
 */
fun File.getFileUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", this)
    } else {
        Uri.fromFile(this)
    }
}

/**
 * 根据Uri将文件保存File到沙盒中
 * 此方法能解决部分Uri无法获取到File的问题
 * 但是会造成文件冗余，可以根据实际情况，决定是否需要删除
 */
fun Uri.saveFileByUri(context: Context): File? {
    try {
        val inputStream = context.contentResolver.openInputStream(this)
        val file = File(context.getExternalFilesDir("Temp"), "${System.currentTimeMillis()}.${this.getExtensionByUri(context)}")
        val fos = FileOutputStream(file)
        val bis = BufferedInputStream(inputStream)
        val bos = BufferedOutputStream(fos)
        val byteArray = ByteArray(1024)
        var bytes = bis.read(byteArray)
        while (bytes > 0) {
            bos.write(byteArray, 0, bytes)
            bos.flush()
            bytes = bis.read(byteArray)
        }
        bos.close()
        fos.close()
        return file
    } catch (e: Exception) {

    }
    return null
}

/**
 * 根据Uri获取File，AndroidQ及以上可用
 * AndroidQ中只有沙盒中的文件可以直接根据绝对路径获取File，非沙盒环境是无法根据绝对路径访问的
 * 因此先判断Uri是否是沙盒中的文件，如果是直接拼接绝对路径访问，否则使用[saveFileByUri]复制到沙盒中生成File
 */
fun Uri.getFileFromUriQ(context: Context): File? {
    var file: File? = null
    if (DocumentsContract.isDocumentUri(context, this)) {
        val uriId = DocumentsContract.getDocumentId(this)
        Log.i("FileExt", "getFileFromUriQ: ${DocumentsContract.getDocumentId(this)}")
        val split: List<String> = uriId.split(":")
        //文件存在沙盒中，可直接拼接全路径访问
        //判断依据目前是Android/data/包名，不够严谨
        if (split.size > 1 && split[1].contains("Android/data/${context.packageName}")) {
            //AndroidQ无法通过Environment.getExternalStorageDirectory()获取SD卡根目录，因此直接/storage/emulated/0/拼接
            file = File("/storage/emulated/0/${split[1]}")
        }
    }
    val flag = file?.exists() ?: false
    return if (!flag) {
        this.saveFileByUri(context)
    } else {
        file
    }
}

/**
 * 根据Uri获取File，AndroidN~AndroidQ可用
 */
fun Uri.getFileFromUriN(context: Context): File? {
    var file: File? = null
    var uri = this
    Log.i("FileExt", "getFileFromUriN: $uri ${uri.authority} ${uri.path}")
    val authority = uri.authority
    val path = uri.path
    /**
     * media类型的Uri，形如content://media/external/images/media/11560
     */
    if (file == null && authority != null && authority.startsWith("media")) {
        uri.getDataColumn(context)?.run {
            file = File(this)
        }
    }
    /**
     * fileProvider授权的Uri
     */
    if (file == null && authority != null && authority.startsWith(context.packageName) && path != null) {
        //这里的值来自你的provider_paths.xml，如果不同需要自己进行添加修改
        val externals = mutableListOf(
            "/external",
            "/external_path",
            "/beta_external_files_path",
            "/external_cache_path",
            "/beta_external_path",
            "/external_files",
            "/internal"
        )
        externals.forEach {
            if (path.startsWith(it)) {
                //如果你在provider_paths.xml中修改了path，需要自己进行修改
                val newFile = File("${Environment.getExternalStorageDirectory().absolutePath}/${path.replace(it, "")}")
                if (newFile.exists()) {
                    file = newFile
                }
            }
        }
    }
    /**
     * Intent.ACTION_OPEN_DOCUMENT选择的文件Uri
     */
    if (file == null && DocumentsContract.isDocumentUri(context, this)) {
        val uriId = DocumentsContract.getDocumentId(this)
        Log.i("FileExt", "isDocumentUri: ${DocumentsContract.getDocumentId(this)}")
        val split: List<String> = uriId.split(":")
        when (uri.authority) {
            "com.android.externalstorage.documents" -> { //内部存储设备中选择
                if (split.size > 1) file = File("${Environment.getExternalStorageDirectory().absolutePath}/${split[1]}")
            }
            "com.android.providers.downloads.documents" -> { //下载内容中选择
                if (uriId.startsWith("raw:")) {
                    file = File(split[1])
                }
                //content://com.android.providers.downloads.documents/document/582
            }
            "com.android.providers.media.documents" -> { //多媒体中选择
                var contentUri: Uri? = null
                when (split[0]) {
                    "image" -> {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    "video" -> {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                    "audio" -> {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                }
                contentUri?.run {
                    if (split.size > 1) {
                        uri = ContentUris.withAppendedId(this, split[1].toLong())
                        Log.i("FileExt", "isDocumentUri media: $uri")
                        uri.getDataColumn(context)?.run {
                            file = File(this)
                        }
                    }
                }
            }
        }
    }
    val flag = file?.exists() ?: false
    return if (!flag) {
        //形如content://com.android.providers.downloads.documents/document/582的下载内容中的文件
        //无法根据Uri获取到真实路径的文件，统一使用saveFileByUri(context)方法获取File
        uri.saveFileByUri(context)
    } else {
        file
    }
}

/**
 * 根据Uri查询文件路径
 * Android4.4之前都可用，Android4.4之后只有从多媒体中选择的文件可用
 */
fun Uri?.getDataColumn(context: Context): String? {
    if (this == null) return null
    var str: String? = null
    var cursor: Cursor? = null
    try {
        cursor = context.contentResolver.query(this, arrayOf(MediaStore.MediaColumns.DATA), null, null, null)
        cursor?.run {
            if (this.moveToFirst()) {
                val index = this.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                if (index != -1) str = this.getString(index)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        cursor?.close()
    }
    return str
}

/**
 * 根据Uri获取MimeType
 */
fun Uri.getMimeTypeByUri(context: Context) =
    context.contentResolver.getType(this)

/**
 * 根据Uri获取扩展名
 */
fun Uri.getExtensionByUri(context: Context) =
    this.getMimeTypeByUri(context)?.getExtensionByMimeType()

/**
 * 根据文件名获取扩展名
 */
fun String.getExtensionByFileName() =
    this.getMimeTypeByFileName().getExtensionByMimeType()

/**
 * 根据文件名获取MimeType
 */
fun String.getMimeTypeByFileName(): String =
    URLConnection.getFileNameMap().getContentTypeFor(this)

/**
 * 根据MimeType获取拓展名
 */
fun String.getExtensionByMimeType() =
    MimeTypeMap.getSingleton().getExtensionFromMimeType(this)

/**
 * 将图片保存至相册，兼容AndroidQ
 */
fun File?.saveToAlbum(context: Context): Boolean {
    if (this == null) return false
    Log.i("FileExt", "saveToAlbum: ${this.absolutePath}")
    runCatching {
        val values = ContentValues()
        val resolver = context.contentResolver;
        val fileName = this.name
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        values.put(MediaStore.MediaColumns.MIME_TYPE, fileName.getMimeTypeByFileName())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //AndroidQ更新图库需要将拍照后保存至沙盒的原图copy到系统多媒体
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            val saveUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (saveUri != null) {
                val out = resolver.openOutputStream(saveUri)
                val input = FileInputStream(this)
                if (out != null) {
                    FileUtils.copy(input, out) //直接调用系统方法保存
                }
                out?.close()
                input.close()
            }
        } else {
            //AndroidQ以下直接将拍照后得到的文件路径插入多媒体中即可
            values.put(MediaStore.MediaColumns.DATA, this.absolutePath)
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        return true
    }.onFailure {
        it.printStackTrace()
    }
    return false
}

/**
 * 判断公有目录文件否存在，自Android Q开始，公有目录File API都失效，不能直接通过new File(path).exists();判断公有目录文件是否存在
 */
fun Uri?.isFileExists(context: Context): Boolean {
    if (this == null) return false
    Log.i("FileExt", "isFileExists: $this")
    var afd: AssetFileDescriptor? = null
    return try {
        afd = context.contentResolver.openAssetFileDescriptor(this, "r")
        afd != null
    } catch (e: FileNotFoundException) {
        false
    } finally {
        afd?.close()
    }
}