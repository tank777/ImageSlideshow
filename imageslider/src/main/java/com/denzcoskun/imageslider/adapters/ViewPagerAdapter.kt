package com.denzcoskun.imageslider.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.denzcoskun.imageslider.R
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.transformation.RoundedTransformation
import com.squareup.picasso.Picasso

/**
 * Created by Deniz Coşkun on 6/23/2020.
 * denzcoskun@hotmail.com
 * İstanbul
 */
class ViewPagerAdapter(
    context: Context?,
    imageList: List<SlideModel>,
    private var radius: Int,
    private var errorImage: Int,
    private var placeholder: Int
) : PagerAdapter() {

    private var imageList: List<SlideModel>? = imageList
    private var layoutInflater: LayoutInflater? =
        context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    private var itemClickListener: ItemClickListener? = null
    private var touchListener: TouchListener? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return imageList!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val itemView = layoutInflater!!.inflate(R.layout.pager_row, container, false)

        val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        val textView = itemView.findViewById<TextView>(R.id.text_view)

        // Image from url or local path check.
        val loader = if (imageList!![position].imageUrl == null) {
            Picasso.get().load(imageList!![position].imagePath!!)
        } else {
            Picasso.get().load(imageList!![position].imageUrl!!)
        }

        loader.transform(RoundedTransformation(radius, 0))
            .placeholder(placeholder)
            .error(errorImage)
            .into(imageView)

        container.addView(itemView)

        imageView.setOnClickListener { itemClickListener?.onItemSelected(position) }
        textView.text = imageList!![position].title
        return itemView
    }

    /**
     * Get layout gravity value from textAlign variable
     *
     * @param  textAlign  text align by user
     */
    fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "RIGHT" -> {
                Gravity.RIGHT
            }
            "CENTER" -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.LEFT
            }
        }
    }

    /**
     * Set item click listener
     *
     * @param  itemClickListener  callback by user
     */
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    /**
     * Set touch listener for listen to image touch
     *
     * @param  touchListener  interface callback
     */
    fun setTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

}