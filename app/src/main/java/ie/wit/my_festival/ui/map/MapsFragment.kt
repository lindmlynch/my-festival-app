package ie.wit.my_festival.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.my_festival.R

class MapsFragment : Fragment() {

    private lateinit var mapsViewModel: MapsViewModel
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        val loc = LatLng(53.3498, -6.2603)

        mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
        mapsViewModel.map.addMarker(MarkerOptions().position(loc).title("You are Here!"))
        mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 8f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}