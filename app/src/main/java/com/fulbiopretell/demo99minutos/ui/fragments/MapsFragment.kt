package com.fulbiopretell.demo99minutos.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fulbiopretell.demo99minutos.R
import com.fulbiopretell.demo99minutos.common.Constants.LAT_LONG_CURRENT
import com.fulbiopretell.demo99minutos.common.Constants.LIST_PLACES
import com.fulbiopretell.demo99minutos.databinding.FragmentListBinding
import com.fulbiopretell.demo99minutos.databinding.FragmentMapsBinding
import com.fulbiopretell.demo99minutos.model.Result
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapsFragment : Fragment() {

    private var listPlaces: List<Result>? = null
    private var latLongCurrent: LatLng? = null
    private var mMap: GoogleMap? = null
    private var mMarker: Marker? = null

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        latLongCurrent?.let {
            drawNearbyPlaces()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            listPlaces = Gson().fromJson(it?.getString(LIST_PLACES), object : TypeToken<List<Result?>?>() {}.getType())
            latLongCurrent = Gson().fromJson(it?.getString(LAT_LONG_CURRENT), LatLng::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun drawNearbyPlaces(){

            val currentLocation = LatLng(latLongCurrent?.latitude!!, latLongCurrent?.longitude!!)

            if (mMarker != null) {
                mMarker!!.remove()
            }

            val markerOptions = MarkerOptions()
                .position(currentLocation)
                .title("Your Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            mMap?.let {
                mMarker = mMap?.addMarker(markerOptions)

                listPlaces?.forEachIndexed { index, result ->
                    val markerOptions2 = MarkerOptions()
                    val googlePlace = result
                    val lat = googlePlace.geometry?.location?.lat
                    val lng = googlePlace.geometry?.location?.lng
                    val placeName = googlePlace.name
                    val latLng = LatLng(lat ?: 0.0, lng ?: 0.0)

                    markerOptions2.position(latLng)
                    markerOptions2.title(placeName)
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                    markerOptions2.snippet(index.toString())

                    mMap?.addMarker(markerOptions2)
                }

                val cameraPosition = CameraPosition.Builder().target(latLongCurrent).zoom(15f).build()
                mMap?.isMyLocationEnabled = true
                mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
    }
}