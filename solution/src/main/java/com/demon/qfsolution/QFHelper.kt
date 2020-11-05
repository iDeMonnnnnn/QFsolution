package com.demon.qfsolution

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.demon.qfsolution.activity.QFBigImgActivity
import com.demon.qfsolution.activity.QFImgsActivity

/**
 * @author DeMon
 * Created on 2020/11/4.
 * E-mail 757454343@qq.com
 * Desc:
 */
class QFHelper {

    var loadNum = 30
    var maxNum = 1
    var isNeedGif = false
    var spanCount = 3

    var isNeedCamera = true

    /**
     * 是否单选
     */
    fun isSinglePick() = maxNum == 1

    /**
     * 设置是否需要需要显示拍照选项，默认true
     */
    fun isNeedCamera(flag: Boolean = true): QFHelper {
        this.isNeedCamera = flag
        return this
    }

    /**
     * 每行显示多少张图片，默认&建议：3
     * 可根据手机分辨率实际情况大小进行调整
     */
    fun setSpanCount(num: Int = 3): QFHelper {
        this.spanCount = num
        return this
    }

    /**
     * 设置分页加载每次加载多少张图片，默认&建议：30
     * 可根据手机分辨率实际情况大小进行调整
     * 注意：该值最少应该保证首次加载充满全屏，否则无法加载更多
     */
    fun setLoadNum(num: Int = 30): QFHelper {
        this.loadNum = num
        return this
    }

    /**
     * 设置可选择最多maxNum张图片
     */
    fun setMaxNum(num: Int = 1): QFHelper {
        this.maxNum = num
        return this
    }

    /**
     * 设置是否需要Gif
     */
    fun isNeedGif(flag: Boolean = false): QFHelper {
        this.isNeedGif = flag
        return this
    }

    /**
     * AppCompatActivity中启动图片选择
     */
    fun start(activity: FragmentActivity, requestCode: Int) {
        if (maxNum < 1) {
            Toast.makeText(activity, activity.getString(R.string.less_one), Toast.LENGTH_LONG).show()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(activity, activity.getString(R.string.qf_storage_permission), Toast.LENGTH_LONG).show()
                return
            }
        }
        val intent = Intent(activity, QFImgsActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    /**
     * Fragment中启动图片选择
     */
    fun start(fragment: Fragment, requestCode: Int) {
        val context = fragment.requireContext()
        if (maxNum < 1) {
            Toast.makeText(context, context.getString(R.string.less_one), Toast.LENGTH_LONG).show()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, context.getString(R.string.qf_storage_permission), Toast.LENGTH_LONG).show()
                return
            }
        }
        val intent = Intent(context, QFImgsActivity::class.java)
        fragment.startActivityForResult(intent, requestCode)
    }


    /**
     * 打开图片浏览器
     * @param uri 图片URI
     */
    fun startImgBrowse(activity: FragmentActivity, uri: Uri, requestCode: Int = EXTRA_RC_IB) {
        val intent = Intent(activity, QFBigImgActivity::class.java)
        intent.putExtra(EXTRA_IMG, uri)
        activity.startActivityForResult(intent, requestCode)
    }


    fun startImgBrowse(fragment: Fragment, uri: Uri, requestCode: Int = EXTRA_RC_IB) {
        val intent = Intent(fragment.requireContext(), QFBigImgActivity::class.java)
        intent.putExtra(EXTRA_IMG, uri)
        fragment.startActivityForResult(intent, requestCode)
    }

    /**
     * 打开图片浏览器
     * @param url 图片链接或者本地路径
     */
    fun startImgBrowse(activity: FragmentActivity, url: String, requestCode: Int = EXTRA_RC_IB) {
        val intent = Intent(activity, QFBigImgActivity::class.java)
        intent.putExtra(EXTRA_IMG, url)
        activity.startActivityForResult(intent, requestCode)
    }


    fun startImgBrowse(fragment: Fragment, url: String, requestCode: Int = EXTRA_RC_IB) {
        startImgBrowse(fragment.requireActivity(), url, requestCode)
    }

    /**
     * 获取选取图片后的结果
     */
    fun getResult(data: Intent?): ArrayList<Uri>? {
        return data?.getParcelableArrayListExtra(EXTRA_RESULT)
    }

    /**
     * 单例
     */
    companion object {
        const val EXTRA_RESULT = "qf.result"
        const val EXTRA_IMG = "qf.img"
        const val EXTRA_RC_IB = 0x1024

        @Volatile
        private var instance: QFHelper? = null

        @JvmStatic
        fun getInstance(): QFHelper {
            return instance ?: synchronized(this) {
                instance ?: QFHelper().also { instance = it }
            }
        }
    }
}