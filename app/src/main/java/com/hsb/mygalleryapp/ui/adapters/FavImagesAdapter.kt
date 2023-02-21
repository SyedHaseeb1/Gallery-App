package com.hsb.mygalleryapp.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hsb.mygalleryapp.data.Models.FavModel
import com.hsb.mygalleryapp.R
import com.hsb.mygalleryapp.Utils.setThumbnail
import com.hsb.mygalleryapp.databinding.GalleryrowBinding

class FavImagesAdapter(private var mList: List<FavModel>) :
        RecyclerView.Adapter<FavImagesAdapter.ViewHolder>() {

    private var ctx: Context? = null
    var favClickLister: ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            GalleryrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ctx = view.root.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mList[position].id
        val uri = Uri.parse(mList[position].uri)
        with(holder.binding) {
            imageNameTxt.text = name.toString()
            imageView.setThumbnail(uri, 0.8f)
            ctx?.let { setHeart(favImageView, it) }
            favImageView.setOnClickListener { favClickLister?.invoke(position) }
            imageView.setOnClickListener {
               // imageClick?.invoke(position,"", uri, 0)
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

    fun updateList(newList: List<FavModel>) {
        this.mList = newList
        notifyDataSetChanged()
    }

    private fun setHeart(imageView: ImageView, context: Context) {
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.heart))
    }
}