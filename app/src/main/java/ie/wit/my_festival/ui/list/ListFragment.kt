
package ie.wit.my_festival.ui.list

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.my_festival.R
import ie.wit.my_festival.adapters.FestivalAdapter
import ie.wit.my_festival.adapters.FestivalListener
import ie.wit.my_festival.databinding.FragmentListBinding
import ie.wit.my_festival.main.FestivalApp
import ie.wit.my_festival.models.FestivalModel
import ie.wit.my_festival.utils.SwipeToDeleteCallback

class ListFragment : Fragment(), FestivalListener {

    lateinit var app: FestivalApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!

    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableFestivalsList.observe(viewLifecycleOwner, Observer {

                festivals ->
            festivals?.let { render(festivals as ArrayList<FestivalModel>) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToFestivalFragment()
            findNavController().navigate(action)
        }

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as FestivalAdapter
                adapter.removeAt(viewHolder.adapterPosition)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(festivals: ArrayList<FestivalModel>){
        fragBinding.recyclerView.adapter = FestivalAdapter(festivals,this)
        if (festivals.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.festivalsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.festivalsNotFound.visibility = View.GONE
        }
    }

    override fun onFestivalClick(festival: FestivalModel, position: Int) {
        val action = ListFragmentDirections.actionListFragmentToFestivalDetailFragment(festival.id)
        findNavController().navigate(action)
    }
    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
