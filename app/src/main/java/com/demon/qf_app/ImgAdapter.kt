package com.demon.qf_app

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author DeMon
 * Created on 2022/4/27.
 * E-mail idemon_liu@qq.com
 * Desc:
 */
class ImgAdapter constructor(val _datas: MutableList<Uri> = mutableListOf()) : RecyclerView.Adapter<ImgAdapter.Holder>() {

    var datas = _datas
       set(value) {
           datas.clear()
           datas.addAll(value)
           notifyDataSetChanged()
       }

    class Holder(root: View) : RecyclerView.ViewHolder(root) {
        val img: ImageView = root.findViewById(R.id.ivImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.img.setImageURI(_datas[position])
    }

    override fun getItemCount(): Int = _datas.size

}