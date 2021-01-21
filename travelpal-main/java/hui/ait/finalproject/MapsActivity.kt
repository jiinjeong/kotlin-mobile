package hui.ait.finalproject

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import hui.ait.finalproject.data.DataPreferences
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    MyLocationProvider.OnNewLocationAvailable{

    private lateinit var mMap: GoogleMap
    private lateinit var myLocationProvider: MyLocationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        searchButton.setOnClickListener {
            onMapSearch(it)
        }
    }

    override fun onStart() {
        super.onStart()
        startLocation()
    }

    @WithPermissions(
        permissions = [android.Manifest.permission.ACCESS_FINE_LOCATION]
    )
    fun startLocation() {
        myLocationProvider = MyLocationProvider(this, this)
        myLocationProvider.startLocationMonitoring()
    }

    override fun onStop() {
        super.onStop()
        myLocationProvider.stopLocationMonitoring()
    }

    override fun onNewLocation(location: Location) {
//        placeInfo.text = "Loc: ${location.latitude}, ${location.longitude}"
    }

    fun onMapSearch(v: View) {
        val location = searchLocation.text.toString()
        var addressList: List<Address>? = null
        try {
            if (location != null || location != "") {
                val geocoder = Geocoder(this)
                addressList = geocoder.getFromLocationName(location, 1)

                val address = addressList!![0]
                val latLng = LatLng(address.getLatitude(), address.getLongitude())
                mMap.addMarker(MarkerOptions().position(latLng).title(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            } else {
                searchLocation.error = getString(R.string.location_error)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions(json))

        val budapest = LatLng(47.4979, 19.0402)
        mMap.addMarker(MarkerOptions().position(budapest).title(getString(R.string.budapest)))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(budapest))

        val cameraPosition = CameraPosition.Builder()
            .target(budapest)
            .zoom(5f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerClickListener {
            var intentActions = Intent()
            intentActions.setClass(this@MapsActivity,
                ActionsActivity::class.java)
            DataPreferences.selectedLoc = it.position
            intentActions.putExtra(KEY_NAME, it.title.toString())
            startActivityForResult(intentActions, REQUEST_DETAILS)
            true
        }
    }

    companion object {
        public val KEY_NAME = "KEY_NAME"
        public val LOCATION = LatLng(10.0, 10.0)
        public val REQUEST_DETAILS = 1001
        public val json =
                "        [\n" +
                "        {\n" +
                "        \"featureType\": \"landscape.natural.landcover\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        },\n" +
                "        {\n" +
                "        \"saturation\": \"15\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"landscape.natural.terrain\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"saturation\": \"20\"\n" +
                "        },\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.attraction\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.business\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"gamma\": \"1.00\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.business\",\n" +
                "        \"elementType\": \"labels.text\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.business\",\n" +
                "        \"elementType\": \"labels.text.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.business\",\n" +
                "        \"elementType\": \"labels.text.stroke\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.business\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        },\n" +
                "        {\n" +
                "        \"hue\": \"#007aff\"\n" +
                "        },\n" +
                "        {\n" +
                "        \"saturation\": \"22\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.government\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#faefd9\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.medical\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#faefd9\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.medical\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.medical\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.park\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#c7e281\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.park\",\n" +
                "        \"elementType\": \"labels.text\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.park\",\n" +
                "        \"elementType\": \"labels.text.stroke\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.park\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.place_of_worship\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#e4e3de\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.place_of_worship\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.school\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#e4e3de\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.school\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.sports_complex\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#c7e281\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.sports_complex\",\n" +
                "        \"elementType\": \"geometry.stroke\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.sports_complex\",\n" +
                "        \"elementType\": \"labels.text.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.sports_complex\",\n" +
                "        \"elementType\": \"labels.text.stroke\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"poi.sports_complex\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.highway\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#fff0ab\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.highway\",\n" +
                "        \"elementType\": \"labels.icon\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.highway.controlled_access\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#ffd88d\"\n" +
                "        },\n" +
                "        {\n" +
                "        \"saturation\": \"1\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.arterial\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.arterial\",\n" +
                "        \"elementType\": \"labels.text.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"road.local\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"simplified\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"transit.station.airport\",\n" +
                "        \"elementType\": \"geometry.fill\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"saturation\": \"0\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"transit.station.bus\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"transit.station.rail\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"off\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"water\",\n" +
                "        \"elementType\": \"all\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"color\": \"#7fd1fe\"\n" +
                "        }\n" +
                "        ]\n" +
                "        },\n" +
                "        {\n" +
                "        \"featureType\": \"water\",\n" +
                "        \"elementType\": \"labels\",\n" +
                "        \"stylers\": [\n" +
                "        {\n" +
                "        \"visibility\": \"on\"\n" +
                "        }\n" +
                "        ]\n" +
                "        }\n" +
                "        ]"
    }
}
