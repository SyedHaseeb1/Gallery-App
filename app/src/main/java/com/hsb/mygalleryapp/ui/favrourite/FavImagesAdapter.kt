package com.hsb.mygalleryapp.ui.favrourite

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsb.mygalleryapp.Models.FavModel
import com.hsb.mygalleryapp.Models.ImagesModel
import com.hsb.mygalleryapp.Utils.setImage
import com.hsb.mygalleryapp.databinding.GalleryrowBinding

class FavImagesAdapter(private var mList: ArrayList<FavModel>) :
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
            imageView.setImage(uri)
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
}