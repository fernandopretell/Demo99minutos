package com.fulbiopretell.demo99minutos.ui.main.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.fulbiopretell.core.extensions.NumericConversionUtils
import com.fulbiopretell.demo99minutos.R
import com.fulbiopretell.demo99minutos.core.extensions.loadUrl
import com.fulbiopretell.demo99minutos.model.Result
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import java.util.*

class PlacesAdapter(val ctx: Context, val listener: PlacesAdapterListener, val currentLocation: LatLng?) : androidx.recyclerview.widget.RecyclerView.Adapter<PlacesAdapter.HomeAdapterViewHolder>() {

    private val items: ArrayList<Result> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return HomeAdapterViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle?.text = item.name
        holder.tvAddress?.text = item.vicinity
        //holder.ivImage?.loadUrl(getPhotoOfPlace(item.photos?.firstOrNull()?.photoReference ?: "", 1000))
        if (item.photos != null && item.photos?.size ?: 0 > 0) {
            holder.ivImage?.loadUrl(getPhotoOfPlace(item.photos?.firstOrNull()?.photoReference ?: "", 1000), error = ctx.resources.getDrawable(R.drawable.ic_baseline_place_24))
        }

        holder.tvDistance?.text = NumericConversionUtils.getFormattedNumber(item.distanceFromCurrent?.toLong() ?: 0L) + " Mtrs"
        holder.rating_bar?.rating = item.rating?.toFloat() ?: 0f
        if (item.userRatingsTotal != null) {
            holder.tvCountReviews?.text = "${item.userRatingsTotal} opiniones"
        } else {
            holder.tvCountReviews?.text = "Sin opiniones"
        }

        if (item.openingHours?.openNow ?: false) {
            holder.tvOpeningNow?.text = "Abierto"
            holder.tvOpeningNow?.setTextColor(ctx.resources.getColor(R.color.core_colorPrimary))
        } else {
            holder.tvOpeningNow?.text = "Cerrado"
            holder.tvOpeningNow?.setTextColor(ctx.resources.getColor(R.color.core_colorRed))
        }

        holder.container?.setOnClickListener {
            listener.onclickPlaceItem(item)
        }
    }

    fun updateData(newData: List<Result>?) {
        items.clear()
        items.addAll(newData!!)
        notifyDataSetChanged()
    }

    inner class HomeAdapterViewHolder(itemView: View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        var container = itemView?.findViewById<ConstraintLayout>(R.id.container)
        var tvTitle = itemView?.findViewById<TextView>(R.id.tvTitle)
        var ivImage = itemView?.findViewById<ImageView>(R.id.ivPhoto)
        var tvDistance = itemView?.findViewById<TextView>(R.id.tvDistance)
        var tvOpeningNow = itemView?.findViewById<TextView>(R.id.tvOpeningNow)
        var rating_bar = itemView?.findViewById<RatingBar>(R.id.rating_bar)
        var tvCountReviews = itemView?.findViewById<TextView>(R.id.tvCountReviews)
        var tvAddress = itemView?.findViewById<TextView>(R.id.tvAddress)
    }

    private fun getPhotoOfPlace(photoReference: String, maxWidth: Int): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("?photoreference=$photoReference")
        url.append("&key=AIzaSyD9cf2d12F-J30dOMlVzVpg9g3UPTUpQlY")
        Timber.e("URL_PHOTO" + url.toString())
        return url.toString()
    }

    interface PlacesAdapterListener {
        fun onclickPlaceItem(item: Result)
    }
}