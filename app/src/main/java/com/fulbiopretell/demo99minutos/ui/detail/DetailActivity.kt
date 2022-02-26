package com.fulbiopretell.demo99minutos.ui.detail

import android.os.Bundle
import com.fulbiopretell.base.BaseActivity
import com.fulbiopretell.demo99minutos.R
import com.fulbiopretell.demo99minutos.common.Constants.PLACE
import com.fulbiopretell.demo99minutos.core.extensions.loadUrl
import com.fulbiopretell.demo99minutos.databinding.ActivityDetailBinding
import com.fulbiopretell.demo99minutos.model.Result
import com.google.gson.Gson
import timber.log.Timber

class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var placeSelected: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()
        setupViews()
    }

    private fun setupData() {
        placeSelected = Gson().fromJson(intent.getStringExtra(PLACE), Result::class.java)
    }

    private fun setupViews() {
        binding.tvTitle.text = placeSelected?.name
        binding.tvAddress.text = placeSelected?.vicinity
        if (placeSelected?.photos != null && placeSelected?.photos?.size ?: 0 > 0) {
            binding.ivImage.loadUrl(getPhotoOfPlace(placeSelected?.photos?.firstOrNull()?.photoReference ?: "", 1000), error = resources.getDrawable(R.drawable.ic_search_128))
        }

        binding.ratingBar.rating = placeSelected?.rating?.toFloat() ?: 0f


        if (placeSelected?.openingHours?.openNow ?: false) {
            binding.placeOpenHour.text = "Abierto"
            binding.placeOpenHour.setTextColor(resources.getColor(R.color.core_colorPrimary))
        } else
            binding.placeOpenHour.text = "Cerrado"
        binding.placeOpenHour.setTextColor(resources.getColor(R.color.core_colorRed))
    }

    private fun getPhotoOfPlace(photoReference: String, maxWidth: Int): String {
        val url = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("?photoreference=$photoReference")
        url.append("&key=AIzaSyD9cf2d12F-J30dOMlVzVpg9g3UPTUpQlY")
        Timber.e("URL_PHOTO" + url.toString())
        return url.toString()
    }
}



