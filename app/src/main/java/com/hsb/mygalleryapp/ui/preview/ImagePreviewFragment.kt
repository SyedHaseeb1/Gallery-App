package com.hsb.mygalleryapp.ui.preview


import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.hsb.mygalleryapp.data.Models.ImagesModel
import com.hsb.mygalleryapp.R
import com.hsb.mygalleryapp.Utils.imageName
import com.hsb.mygalleryapp.Utils.toast
import com.hsb.mygalleryapp.Utils.zoom
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import com.hsb.mygalleryapp.databinding.FragmentImagePreviewBinding
import com.hsb.mygalleryapp.ui.adapters.ViewPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class ImagePreviewFragment : Fragment() {

    private val args: ImagePreviewFragmentArgs by navArgs()
    private lateinit var binding: FragmentImagePreviewBinding
    private val viewModel: AppViewModel by inject()
    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>

    private var uri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity?)!!.supportActionBar!!.title = args.name

        with(binding) {
            uri = Uri.parse(args.uri)
            val position = args.position
            viewPagerFun(position)
            imageName = {
                (activity as AppCompatActivity?)!!.supportActionBar!!.title = it
            }
            viewModel.deleteAction = {
                context?.let { discardAlert(it) }

            }

            launchDeleteRequest()
        }
    }

    private fun viewPagerFun(position: Int) {
        val imagesArray = ArrayList<ImagesModel>()
        imagesArray.clear()
        imagesArray.addAll(viewModel.folderImages)
        val adapter = ViewPagerAdapter(imagesArray, position)
        with(binding.viewpager) {
            this.adapter = adapter
            this.clipToPadding = true
            this.clipChildren = false
            this.offscreenPageLimit = 20
            this.currentItem = position
            this.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER

        }
        binding.viewpager.setCurrentItem(position, false)
        zoom = {
            binding.viewpager.isUserInputEnabled = it
        }
    }

    private fun discardAlert(context: Context) {
        val dialogueBinding = layoutInflater.inflate(R.layout.delete_dialogue, null)
        val myDialogue = Dialog(context)
        myDialogue.setContentView(dialogueBinding)
        myDialogue.setCancelable(true)
        myDialogue.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialogue.show()
        val noBtn = myDialogue.findViewById<Button>(R.id.cancelbtn)
        val yesBtn = myDialogue.findViewById<Button>(R.id.deletebtn)
        noBtn.setOnClickListener {
            myDialogue.dismiss()
        }
        yesBtn.setOnClickListener {
            deleteImages(args.id, context)
            // activity?.onBackPressed()
            myDialogue.dismiss()
        }
    }

    fun deleteImages(id: Long, context: Context) {
        val Uri_one =
            ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            CoroutineScope(Dispatchers.Main).launch {
                deletePhotoFromExternalStorage(Uri_one)
            }

        }
    }

    private fun launchDeleteRequest() {
        intentSenderLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        //Update List
                    }
                    context?.toast("Media deleted successfully")

                } else {
                    context?.toast("Media couldn't be deleted")
                }
            }
    }

    private suspend fun deletePhotoFromExternalStorage(uri: Uri) {
        withContext(Dispatchers.IO) {
            val intentSender = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                    activity?.contentResolver?.let {
                        MediaStore.createDeleteRequest(
                            it,
                            listOf(uri)
                        ).intentSender
                    }
                }
                else -> null
            }
            intentSender?.let { sender ->
                intentSenderLauncher.launch(
                    IntentSenderRequest.Builder(sender).build()
                )
            }
        }
    }

}