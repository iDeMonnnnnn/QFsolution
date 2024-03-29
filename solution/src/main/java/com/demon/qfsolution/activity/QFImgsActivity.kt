package com.demon.qfsolution.activity

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.demon.qfsolution.*
import com.demon.qfsolution.list.QFImgAdapter
import com.demon.qfsolution.bean.QFImgBean
import com.demon.qfsolution.list.HackyGridLayoutManager
import com.demon.qfsolution.list.SpacesItemDecoration
import com.demon.qfsolution.utils.getExtensionByUri
import com.demon.qfsolution.utils.gotoCamera
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * @author DeMon
 * Created on 2020/11/5.
 * E-mail idemon_liu@qq.com
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
        if (!QFHelper.isSinglePick()) {
            btn_qf_ok.text = getString(R.string.qf_ok_value, 0, QFHelper.maxNum)
        }

        if (QFHelper.isNeedCamera) {
            imgList.add(QFImgBean(1))
        }

        cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
        getImgDatas()
        adapter = QFImgAdapter(imgList, object : QFImgAdapter.ImgPickedListener {
            override fun onImgPickedChange(uris: ArrayList<Uri>, size: Int) {
                if (!QFHelper.isSinglePick()) {
                    btn_qf_ok.text = getString(R.string.qf_ok_value, size, QFHelper.maxNum)
                }
            }

            override fun onCameraClick() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this@QFImgsActivity, getString(R.string.qf_camera_permission), Toast.LENGTH_LONG).show()
                        return
                    }
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    gotoCamera<Uri>()?.run {
                        if (!isPickedOver()) {
                            adapter.resultList.add(this)
                            if (!QFHelper.isSinglePick()) {
                                btn_qf_ok.text = getString(R.string.qf_ok_value, adapter.resultList.size, QFHelper.maxNum)
                            }
                        }
                        imgList.add(1, QFImgBean(this, !isPickedOver()))
                        adapter.notifyItemInserted(1)
                    }
                }
            }

            override fun onImgClick(uri: Uri) {
                QFHelper.startImgBrowse(this@QFImgsActivity, uri)
            }
        })
        rv_imgs.setHasFixedSize(true)
        val gridLayoutManager = HackyGridLayoutManager(this, QFHelper.spanCount)
        gridLayoutManager.isSmoothScrollbarEnabled = true
        rv_imgs.layoutManager = gridLayoutManager
        rv_imgs.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelOffset(R.dimen.qf_grid_margin), QFHelper.spanCount))
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

    @SuppressLint("Range")
    private fun getImgDatas() {
        index = 0
        cursor?.run {
            while (moveToNext() && index <= QFHelper.loadNum) {
                val picPath = getString(getColumnIndex(MediaStore.Images.Media.DATA)) ?: ""
                if (TextUtils.isEmpty(picPath) || !File(picPath).exists()) {
                    Log.i(TAG, "getImgDatas: $picPath no exists")
                } else {
                    val id = getLong(getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    if (!QFHelper.isNeedGif && uri.getExtensionByUri() == "gif") {
                        continue
                    }
                    imgList.add(QFImgBean(uri, picPath))
                    index++
                }
            }
        }
        if (index < QFHelper.loadNum) {
            hasImgs = false
            cursor?.close()
        } else {
            hasImgs = true
        }

    }

    fun isPickedOver() = adapter.resultList.size == QFHelper.maxNum

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