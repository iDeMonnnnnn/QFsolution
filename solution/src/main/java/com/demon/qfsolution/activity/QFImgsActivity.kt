package com.demon.qfsolution.activity

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.demon.qfsolution.*
import com.demon.qfsolution.list.QFImgAdapter
import com.demon.qfsolution.bean.QFImgBean
import com.demon.qfsolution.list.HackyGridLayoutManager
import com.demon.qfsolution.list.SpacesItemDecoration
import com.demon.qfsolution.utils.getExtensionByUri
import com.demon.qfsolution.utils.gotoCamera
import com.demon.qfsolution.utils.isFileExists
import com.demon.qfsolution.utils.launchUI
import kotlinx.coroutines.GlobalScope
import java.io.File

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail 757454343@qq.com
 * Desc: 图片选择器
 */
class QFImgsActivity : AppCompatActivity() {
    private val TAG = "QFImgsActivity"
    private val imgList = arrayListOf<QFImgBean>()
    private var cursor: Cursor? = null
    private var hasImgs = true
    private lateinit var adapter: QFImgAdapter
    private var index = 0

    private lateinit var btn_qf_ok: Button
    private lateinit var rv_imgs: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qf_imgs)
        btn_qf_ok = findViewById(R.id.btn_qf_ok)
        rv_imgs = findViewById(R.id.rv_imgs)
        if (!QFHelper.getInstance().isSinglePick()) {
            btn_qf_ok.text = getString(R.string.qf_ok_value, 0, QFHelper.getInstance().maxNum)
        }

        if (QFHelper.getInstance().isNeedCamera) {
            imgList.add(QFImgBean(1))
        }

        cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
        getImgDatas()
        adapter = QFImgAdapter(imgList, object : QFImgAdapter.ImgPickedListener {
            override fun onImgPickedChange(uris: ArrayList<Uri>, size: Int) {
                if (!QFHelper.getInstance().isSinglePick()) {
                    btn_qf_ok.text = getString(R.string.qf_ok_value, size, QFHelper.getInstance().maxNum)
                }
            }

            override fun onCameraClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this@QFImgsActivity, getString(R.string.qf_camera_permission), Toast.LENGTH_LONG).show()
                        return
                    }
                }
                GlobalScope.launchUI {
                    gotoCamera<Uri>(true)?.run {
                        if (!isPickedOver()) {
                            adapter.resultList.add(this)
                            if (!QFHelper.getInstance().isSinglePick()) {
                                btn_qf_ok.text = getString(R.string.qf_ok_value, adapter.resultList.size, QFHelper.getInstance().maxNum)
                            }
                        }
                        imgList.add(1, QFImgBean(this, !isPickedOver()))
                        adapter.notifyItemInserted(1)
                    }
                }
            }

            override fun onImgClick(uri: Uri) {
                QFHelper.getInstance().startImgBrowse(this@QFImgsActivity, uri)
            }
        })
        rv_imgs.setHasFixedSize(true)
        val gridLayoutManager = HackyGridLayoutManager(this, QFHelper.getInstance().spanCount)
        gridLayoutManager.isSmoothScrollbarEnabled = true
        rv_imgs.layoutManager = gridLayoutManager
        rv_imgs.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.qf_grid_margin), QFHelper.getInstance().spanCount))
        rv_imgs.addOnScrollListener(ScrollListener())
        rv_imgs.adapter = adapter

        btn_qf_ok.setOnClickListener {
            if (adapter.resultList.isNullOrEmpty()) {
                Toast.makeText(this, getString(R.string.qf_last_one), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent()
            intent.putExtra(QFHelper.EXTRA_RESULT, adapter.resultList)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun getImgDatas() {
        index = 0
        cursor?.run {
            while (moveToNext() && index <= QFHelper.getInstance().loadNum) {
                val picPath = getString(getColumnIndex(MediaStore.Images.Media.DATA)) ?: ""
                if (TextUtils.isEmpty(picPath) || !File(picPath).exists()) {
                    Log.i(TAG, "getImgDatas: $picPath no exists")
                } else {
                    val id = getLong(getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    if (!QFHelper.getInstance().isNeedGif && uri.getExtensionByUri(this@QFImgsActivity) == "gif") {
                        continue
                    }
                    imgList.add(QFImgBean(uri, picPath))
                    index++
                }
            }
        }
        if (index < QFHelper.getInstance().loadNum) {
            hasImgs = false
            cursor?.close()
        } else {
            hasImgs = true
        }

    }

    fun isPickedOver() = adapter.resultList.size == QFHelper.getInstance().maxNum

    /**
     * 加载更多
     */
    private inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val childCount = recyclerView.childCount
            if (childCount > 0) {
                val lastChild = recyclerView.getChildAt(childCount - 1)
                val itemCount = recyclerView.adapter?.itemCount ?: 0
                val lastVisible = recyclerView.getChildAdapterPosition(lastChild)
                if (lastVisible == itemCount - 1 && hasImgs) {
                    getImgDatas()
                    adapter.notifyItemRangeInserted(itemCount, index)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cursor?.close()
    }


}