package com.hsb.mygalleryapp.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hsb.mygalleryapp.data.Models.ImagesModel
import com.hsb.mygalleryapp.Utils.setThumbnail
import com.hsb.mygalleryapp.databinding.Gallery3rowBinding
import com.hsb.mygalleryapp.Utils.interfaces.AdapterClicks

class GalleryImagesAdapter(
    private var mList: List<ImagesModel>,
    private val interfaceClicks: AdapterClicks
) :
    RecyclerView.Adapter<GalleryImagesAdapter.ViewHolder>() {
    private lateinit var ctx: Context
    var favClickLister: ((Int, ImageView) -> Unit)? = null
    var favColorLister: ((Int, ImageView) -> Unit)? = null
    var setImageInAdapter: ((Int, ImageView, Uri) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryImagesAdapter.ViewHolder {
        val view =
            Gallery3rowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ctx = view.root.context

        return ViewHolder(view)
    }

    var c = 0
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mList[position].name
        val uri = mList[position].uri
        val id = mList[position].imageId
        with(holder.binding) {
            imageNameTxt.text = name
            imageView.setThumbnail(uri, 0.5f)
            favColorLister?.invoke(position, favImageView)
            favImageView.setOnClickListener {
                favClickLister?.invoke(position, favImageView)
            }
            imageView.setOnClickListener {
                interfaceClicks.imageViewClick(position, "", uri, id)
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

    inner class ViewHolder(var binding: Gallery3rowBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun updateList(newList: List<ImagesModel>) {
        this.mList = newList
        notifyDataSetChanged()
    }

}