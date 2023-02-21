package com.hsb.mygalleryapp.ui.favrourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hsb.mygalleryapp.data.Models.FavModel
import com.hsb.mygalleryapp.Utils.gridRv
import com.hsb.mygalleryapp.Utils.toast
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import com.hsb.mygalleryapp.databinding.FragmentFavBinding
import com.hsb.mygalleryapp.ui.adapters.FavImagesAdapter
import org.koin.android.ext.android.inject

class FavFragment : Fragment() {

    private lateinit var binding: FragmentFavBinding
    private val viewModel: AppViewModel by inject()
    private lateinit var allFavImagesAdapter: FavImagesAdapter
    private var allFavImagesArray = ArrayList<FavModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Fav Images"

        with(binding) {
            viewModel.getFavImagesUri().observe(viewLifecycleOwner, Observer {
                allFavImagesArray.clear()
                allFavImagesArray.addAll(it)
                favRV.adapter = allFavImagesAdapter
            })
            allFavImagesAdapter = FavImagesAdapter(allFavImagesArray)
            favRV.layoutManager = context?.gridRv(2)
            allFavImagesAdapter.favClickLister = {
                val node = allFavImagesArray[it]
                val fav = viewModel.addFav(node)
                if (fav) {
                    context?.toast("Added to favourites")
                } else {
                    context?.toast("Removed from favourites")
                }
                viewModel.favColorListerFragment?.invoke(node.uri.toString(), fav)
            }
        }
//        imageClick = { position: Int?, name: String, uri: Uri, l: Long ->
//            val openFragmentWithData =
//                FavFragmentDirections.actionNavigationFavToNavigationPreview(
//                    position!!,
//                    uri.toString(),
//                    0
//                )
//            findNavController().navigate(openFragmentWithData, fragmentAnimation)
//        }
    }
}