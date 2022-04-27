package com.demon.qfsolution.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.R
import com.demon.qfsolution.bean.QFImgBean
import com.demon.qfsolution.loader.QFImgLoader


/**
 * @author DeMon
 * Created on 2020/11/3.
 * E-mail idemon_liu@qq.com
 * Desc:
 */
class QFImgAdapter constructor(private var imgList: MutableList<QFImgBean>, private var listener: ImgPickedListener? = null) : RecyclerView.Adapter<QFImgAdapter.ViewHolder>() {

    val resultList = arrayListOf<Uri>()

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContext = itemView.context
        val qf_pick = itemView.findViewById<ImageView>(R.id.qf_pick)
        val qf_img = itemView.findViewById<ImageView>(R.id.qf_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_qf_img, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            val bean = imgList[position]
            if (bean.type == 1 && position == 0) {
                qf_pick.visibility = View.GONE
                qf_img.setImageResource(R.drawable.ic_qf_camera)
                qf_img.scaleType = ImageView.ScaleType.CENTER
                qf_img.setOnClickListener {
                    listener?.onCameraClick()
                }
            } else {
                qf_pick.visibility = View.VISIBLE
                val uri = bean.uri
                QFImgLoader.getInstance().displayThumbnail(qf_img, uri)
                qf_pick.setImageResource(
                    if (bean.isSelected) {
                        R.drawable.ic_qf_checked
                    } else {
                        R.drawable.qf_unchecked
                    }
                )
                qf_pick.setOnClickListener {
                    if (!bean.isSelected && resultList.size >= QFHelper.maxNum) {
                        Toast.makeText(mContext, mContext.getString(R.string.qf_no_more), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (bean.isSelected) {
                        bean.isSelected = false
                        resultList.remove(uri)
                    } else {
                        bean.isSelected = true
                        resultList.add(uri)
                    }
                    listener?.onImgPickedChange(resultList, resultList.size)
                    notifyItemChanged(position)
                }
                qf_img.setOnClickListener {
                    listener?.onImgClick(uri)
                }
            }
        }

    }

    override fun getItemCount(): Int = imgList.size


    interface ImgPickedListener {
        fun onImgPickedChange(uris: ArrayList<Uri>, size: Int)

        fun onCameraClick()

        fun onImgClick(uri: Uri)
    }
}