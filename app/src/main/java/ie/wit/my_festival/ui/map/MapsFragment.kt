package ie.wit.my_festival.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.my_festival.R
import ie.wit.my_festival.models.FestivalModel
import ie.wit.my_festival.ui.auth.LoggedInViewModel
import ie.wit.my_festival.ui.list.ListViewModel
import ie.wit.my_festival.utils.createLoader
import ie.wit.my_festival.utils.hideLoader
import ie.wit.my_festival.utils.showLoader

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val listViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 8f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            listViewModel.observableFestivalsList.observe(
                viewLifecycleOwner,
                Observer { festivals ->
                    festivals?.let {
                        render(festivals as ArrayList<FestivalModel>)
                        hideLoader(loader)
                    }
                })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)

                val item = menu.findItem(R.id.toggleFestivals) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleFestivals: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleFestivals.isChecked = false

                toggleFestivals.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) listViewModel.loadAll()
                    else listViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
    private fun render(festivalsList: ArrayList<FestivalModel>) {
        var markerColour: Float
        if (festivalsList.isNotEmpty()) {
            mapsViewModel.map.clear()
            festivalsList.forEach {
                markerColour = if(it.email.equals(this.listViewModel.liveFirebaseUser.value!!.email))
                    BitmapDescriptorFactory.HUE_AZURE + 5
                else
                    BitmapDescriptorFactory.HUE_RED

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.title} ${it.date}")
                        .snippet(it.email)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour ))
                )        }
        }
    }
    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Festivals")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) {
                firebaseUser -> if (firebaseUser != null) {
            listViewModel.liveFirebaseUser.value = firebaseUser
            listViewModel.load()
        }        }
    }
}