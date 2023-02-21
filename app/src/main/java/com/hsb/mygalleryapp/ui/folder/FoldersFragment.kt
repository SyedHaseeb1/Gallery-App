package com.hsb.mygalleryapp.ui.folder

import android.net.Uri
import android.os.Bundle
import android.os.FileObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hsb.mygalleryapp.data.Models.FoldersModel
import com.hsb.mygalleryapp.data.Models.ImagesModel
import com.hsb.mygalleryapp.Utils.getRealPath
import com.hsb.mygalleryapp.Utils.gridRv
import com.hsb.mygalleryapp.Utils.imageClick
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import com.hsb.mygalleryapp.databinding.FragmentFoldersBinding
import com.hsb.mygalleryapp.ui.adapters.FoldersAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import kotlin.system.measureTimeMillis


class FoldersFragment : Fragment() {

    private lateinit var binding: FragmentFoldersBinding
    private val viewModel: AppViewModel by inject()
    private lateinit var allFoldersAdapter: FoldersAdapter
    private var allFolderArray = ArrayList<FoldersModel>()
    private var imagesArray = ArrayList<ImagesModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoldersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Gallery"


        with(binding) {
            galleryRV.layoutManager = context?.gridRv(2)
            allFoldersAdapter = FoldersAdapter(allFolderArray)
            galleryRV.adapter = allFoldersAdapter

            getFoldersFun()

            imageClick = { position: Int, s: String, uri: Uri, l: Long ->
                viewModel.folderImages.clear()
                for (item in imagesArray) {
                    if (l == item.folder) {
                        viewModel.folderImages.add(item)
                    }
                }
                viewModel.folderImages.reverse()
                val openFragmentWithData =
                    FoldersFragmentDirections.actionNavigationFolderToNavigationGallery(s, l)
                findNavController().navigate(openFragmentWithData)
            }
        }


    }

    private fun getFoldersFun() {
        var folderIndex = 0
        CoroutineScope(Main).launch {
            val executor = measureTimeMillis {
                val task1 = async {
                    val imageList = ArrayList<ImagesModel>()
                    println("kabootar: task 01 is running on ${Thread.currentThread().name}")
                    viewModel.allImagesArray.observe(viewLifecycleOwner) {
                        imageList.clear()
                        imageList.addAll(it)
                        imagesArray.clear()
                        imagesArray.addAll(it)
                    }
                    imageList
                }.await()


                val result = async {
                    val foldersLst = ArrayList<FoldersModel>()
                    viewModel.allFoldersArray.observe(viewLifecycleOwner) {
                        foldersLst.clear()
                        foldersLst.addAll(it)
                    }
                    val a = foldersLst.map { it.id }
                    a.forEach { id ->
                        val b = task1.filter { it.folder == id }
                        foldersLst[folderIndex].imageC = b.size
                        folderIndex++
                    }

                    folderIndex = 0
                    foldersLst
                }.await()
                allFolderArray.clear()
                allFolderArray.addAll(result)
                result.clear()
            }

            println("kabootar: Result time -> ${executor} ms..")
            allFoldersAdapter.updateList(allFolderArray)
            val path = context?.getRealPath(allFolderArray[0].uri)
            val folder = File(path.toString()).parent
            println("Folder: $folder")
            if (folder != null) {
                MyFileObserver(folder).startWatching()
            }

        }

    }

}

class MyFileObserver(path: String) : FileObserver(path, CREATE) {

    override fun onEvent(event: Int, path: String?) {
        // Your event handling logic here
        println("Folder: $path has been created!")
    }

}