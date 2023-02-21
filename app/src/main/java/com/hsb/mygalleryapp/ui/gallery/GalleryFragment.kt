package com.hsb.mygalleryapp.ui.gallery

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.hsb.mygalleryapp.data.Models.FavModel
import com.hsb.mygalleryapp.data.Models.ImagesModel
import com.hsb.mygalleryapp.R
import com.hsb.mygalleryapp.Utils.gridRv
import com.hsb.mygalleryapp.Utils.toast
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import com.hsb.mygalleryapp.databinding.FragmentGalleryBinding
import com.hsb.mygalleryapp.Utils.interfaces.AdapterClicks
import com.hsb.mygalleryapp.ui.adapters.GalleryImagesAdapter
import org.koin.android.ext.android.inject


class GalleryFragment : Fragment(), AdapterClicks {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: AppViewModel by inject()
    private val args: GalleryFragmentArgs by navArgs()
    private lateinit var previewImagesAdapter: GalleryImagesAdapter
    private var allImagesArray = ArrayList<ImagesModel>()
    private var favArray = ArrayList<FavModel>()

    var page = 0
    var limit = 2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            val folderImages = viewModel.folderImages
            galleryRV.layoutManager = context?.gridRv(3)
            previewImagesAdapter = GalleryImagesAdapter(folderImages, this@GalleryFragment)
            galleryRV.hasFixedSize()
            galleryRV.adapter = previewImagesAdapter

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val mLayoutManager = LinearLayoutManager(context);

                galleryRV.setOnScrollChangeListener { view, x, y, ox, oy ->

                    val linearLayoutManager = galleryRV.layoutManager
                    //    val firstVisiblePosition = linearLayoutManager?.isViewPartiallyVisible(view,true,false)

                    val visibleCount = linearLayoutManager?.childCount
                    val allCount = linearLayoutManager?.itemCount
                    if (visibleCount == folderImages.size - 1) {
                        Log.e("Scroll", "Load More")
                    }
                }
            }


            (activity as AppCompatActivity?)!!.supportActionBar!!.title =
                "${args.name}\t-\t${folderImages.size} Images."



            viewModel.getFavImagesUri().observe(viewLifecycleOwner) {
                favArray.clear()
                favArray.addAll(it)
                previewImagesAdapter.updateList(folderImages)
                previewImagesAdapter.favColorLister = { i: Int, imageView: ImageView ->
                    val allUri = folderImages[i].uri.toString()
                    for (item in favArray) {
                        if (allUri == item.uri) {
                            context?.let { setHeart(imageView, it) }
                            Log.e("Fav", "true")
                        }
                    }

                }
            }
            viewModel.favColorListerFragment = { s: String, b: Boolean ->
                for (item in folderImages) {
                    if (item.uri.toString() == s) {
                        val index = folderImages.indexOf(item)
                        folderImages[index].fav = b
                        viewModel.folderImages[index].fav = b
                    }
                }
                previewImagesAdapter.updateList(folderImages)
            }
            previewImagesAdapter.favClickLister = { i: Int, imageView: ImageView ->
                val uri = folderImages[i].uri
                val favNode = FavModel(uri.toString())
                if (viewModel.addFav(favNode)) {
                    context?.let { setHeart(imageView, it) }
                    folderImages[i].fav = true
                    viewModel.folderImages[i].fav = true
                    context?.toast("Added to favourites")
                } else {
                    context?.let { removeHeart(imageView, it) }
                    folderImages[i].fav = false
                    viewModel.folderImages[i].fav = false
                    context?.toast("Removed from favourites")
                }
            }
        }
    }



    private fun removeHeart(imageView: ImageView, context: Context) {
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.textcolor))
    }

    private fun setHeart(imageView: ImageView, context: Context) {
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.heart))
    }


    override fun imageViewClick(position: Int, name: String, uri: Uri, id: Long) {
        val openFragmentWithData =
            GalleryFragmentDirections.actionNavigationGalleryToNavigationPreview(
                position, name, id
            )
        findNavController().navigate(openFragmentWithData)
    }


}