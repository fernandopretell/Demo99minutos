package com.fulbiopretell.demo99minutos.ui.main

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.fulbiopretell.base.BaseActivity
import com.fulbiopretell.demo99minutos.databinding.ActivityMainBinding
import com.fulbiopretell.demo99minutos.model.MyPlaces
import com.fulbiopretell.demo99minutos.viewmodel.MainViewModel
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import timber.log.Timber
import com.fulbiopretell.demo99minutos.R
import com.fulbiopretell.demo99minutos.common.Constants.LAT_LONG_CURRENT
import com.fulbiopretell.demo99minutos.common.Constants.LIST_PLACES
import com.fulbiopretell.demo99minutos.model.Result
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class MainActivity : BaseActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewmodel: MainViewModel
    var navController: NavController? = null

    //location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    private lateinit var mLastLocation: Location

    private var listPlaces: List<Result>? = null
    private var currentLocation: LatLng? = null

    companion object {
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
        initViewModel()
    }

    private fun initViewModel() {
        viewmodel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun setupNavigation(response: MyPlaces?, latLon: LatLng) {

        val bundle = bundleOf(
            LIST_PLACES to Gson().toJson(response?.results),
            LAT_LONG_CURRENT to Gson().toJson(latLon)
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragNavHost) as NavHostFragment
        navController = navHostFragment.navController
        navController?.navigate(R.id.mapsFragment, bundle)
        binding.navigation.setupWithNavController(navController!!)

        binding.navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mapsFragment -> {
                    val bundle1 = bundleOf(
                        LIST_PLACES to Gson().toJson(listPlaces),
                        LAT_LONG_CURRENT to Gson().toJson(currentLocation)
                    )
                    navController?.navigate(R.id.mapsFragment, bundle1)
                }

                R.id.listFragment -> {
                    val bundle2 = bundleOf(
                        LIST_PLACES to Gson().toJson(listPlaces),
                        LAT_LONG_CURRENT to Gson().toJson(currentLocation)
                    )
                    navController?.navigate(R.id.listFragment, bundle2)
                }
            }
            true
        }

        // Pass the IDs of top-level destinations in AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.mapsFragment,
                R.id.listFragment
            )
        )
    }

    private fun getNearbyPlaces() {

        viewmodel.getNearbyPlaces(getUrl(latitude, longitude)).observe(this, {
            when (it) {
                is MainViewModel.ViewState.Loading -> {
                    showLoading(true)
                }

                is MainViewModel.ViewState.GetDataSuccess -> {
                    showLoading(false)
                    listPlaces = it.response?.results
                    currentLocation = LatLng(latitude, longitude)

                    setupNavigation(it.response, currentLocation!!)
                }

                is MainViewModel.ViewState.GetDataResponseFailure -> {
                    showLoading(false)
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getUrl(latitude: Double, longitude: Double): String {

        val googlePlaceUrl = StringBuilder("maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=5000")
        googlePlaceUrl.append("&key=AIzaSyD9cf2d12F-J30dOMlVzVpg9g3UPTUpQlY")

        return googlePlaceUrl.toString()
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )
            else
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), MY_PERMISSION_CODE
                )

            return false
        } else
            return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallback()

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

                            //mMap.isMyLocationEnabled = true
                        }

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0.locations.size - 1)
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude
                getNearbyPlaces()
            }
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallback()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }

        } else {
            buildLocationRequest()
            buildLocationCallback()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }
}