package com.hsb.mygalleryapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.hsb.mygalleryapp.data.Models.FoldersModel
import com.hsb.mygalleryapp.Utils.imageClick
import com.hsb.mygalleryapp.Utils.setThumbnail
import com.hsb.mygalleryapp.databinding.GalleryrowBinding

class FoldersAdapter(private var mList: List<FoldersModel>) :
    RecyclerView.Adapter<FoldersAdapter.ViewHolder>() {

    private lateinit var ctx: Context
    var favClickLister: ((Int, ImageView) -> Unit)? = null
    var favColorLister: ((Int, ImageView) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            GalleryrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ctx = view.root.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mList[position].name
        val id = mList[position].id
        val uri = mList[position].uri
        val imageCount = mList[position].imageC
        with(holder.binding) {
            imageNameTxt.text = name.toString()
            imageCountTxt.text = imageCount.toString()
            imageView.setThumbnail(uri,0.8f)
            favImageView.isVisible = false

            imageView.setOnClickListener {
                imageClick?.invoke(position,name, uri, id)
            }
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: GalleryrowBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun updateList(newList: List<FoldersModel>) {
        this.mList = newList
        notifyDataSetChanged()
    }
}