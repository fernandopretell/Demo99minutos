package com.fulbiopretell.demo99minutos.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulbiopretell.demo99minutos.R
import com.fulbiopretell.demo99minutos.common.Constants
import com.fulbiopretell.demo99minutos.common.Constants.PLACE
import com.fulbiopretell.demo99minutos.model.Result
import com.fulbiopretell.demo99minutos.ui.detail.DetailActivity
import com.fulbiopretell.demo99minutos.ui.main.adapters.PlacesAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private var listPlaces: List<Result>? = null
    private var latLongCurrent: LatLng? = null
    private var adapter: PlacesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            listPlaces = Gson().fromJson(it?.getString(Constants.LIST_PLACES), object : TypeToken<List<Result?>?>() {}.getType())
            latLongCurrent = Gson().fromJson(it?.getString(Constants.LAT_LONG_CURRENT), LatLng::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initRecyclerView()
    }

    private fun initData() {
        listPlaces?.forEach {
            it.distanceFromCurrent = meterDistanceBetweenPoints(latLongCurrent?.latitude?.toFloat() ?: 0f, latLongCurrent?.longitude?.toFloat() ?: 0f, it.geometry?.location?.lat?.toFloat() ?: 0f, it.geometry?.location?.lng?.toFloat() ?: 0f)
        }
    }

    private fun initRecyclerView() {

        val listener = object : PlacesAdapter.PlacesAdapterListener{
            override fun onclickPlaceItem(item: Result) {
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(PLACE, Gson().toJson(item))
                }
                startActivity(intent)
            }
        }

        recycler.layoutManager = LinearLayoutManager(context)
        adapter = PlacesAdapter(requireContext(), listener, latLongCurrent)
        recycler.adapter = adapter
        adapter?.updateData(listPlaces?.sortedBy { it.distanceFromCurrent })
    }

    private fun meterDistanceBetweenPoints(lat_a: Float, lng_a: Float, lat_b: Float, lng_b: Float): Double {
        val pk = (180f / Math.PI).toFloat()
        val a1 = lat_a / pk
        val a2 = lng_a / pk
        val b1 = lat_b / pk
        val b2 = lng_b / pk
        val t1 = Math.cos(a1.toDouble()) * Math.cos(a2.toDouble()) * Math.cos(b1.toDouble()) * Math.cos(b2.toDouble())
        val t2 = Math.cos(a1.toDouble()) * Math.sin(a2.toDouble()) * Math.cos(b1.toDouble()) * Math.sin(b2.toDouble())
        val t3 = Math.sin(a1.toDouble()) * Math.sin(b1.toDouble())
        val tt = Math.acos(t1 + t2 + t3)
        return 6366000 * tt
    }
}