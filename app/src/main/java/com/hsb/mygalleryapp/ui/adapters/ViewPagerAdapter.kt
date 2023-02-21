package com.hsb.mygalleryapp.ui.adapters

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hsb.mygalleryapp.data.Models.ImagesModel
import com.hsb.mygalleryapp.Utils.*
import com.hsb.mygalleryapp.databinding.ViewpagerlayoutBinding


class ViewPagerAdapter(
    private val mList: ArrayList<ImagesModel>, val sentPosition: Int
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ViewpagerlayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ViewpagerlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pic = mList[position].uri
        val name = mList[position].name
        with(holder.binding) {
            imageview.setImageURI(pic)
            imageName?.invoke(name)

            imageview.setOnTouchListener { v, event ->
                v.onTouchEvent(event)
                println("MyTouch  ${event.action} - ${event.x} - ${event.y}")
                touchInvoke?.invoke(imageview)
                if (imageview.currentZoom > 1) {
                    zoom?.invoke(false)

                    false
                } else {
                    zoom?.invoke(true)
                    false
                }

                return@setOnTouchListener false
            }
            touch(imageview)

        }
    }

    fun touch(view: View) {
        // Obtain MotionEvent object
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 100
        val x = 100.51456f
        val y = 200.5453f
// List of meta states found here:     developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        val metaState = 0
        val motionEvent = MotionEvent.obtain(
            downTime,
            eventTime,
            MotionEvent.ACTION_DOWN,
            x,
            y,
            metaState
        )

// Dispatch touch event to view
        view.dispatchTouchEvent(motionEvent)
        //view.dispatchTouchEvent(motionEvent)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}