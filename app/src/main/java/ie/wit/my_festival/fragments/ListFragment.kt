package ie.wit.my_festival.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.my_festival.R
import ie.wit.my_festival.adapters.FestivalAdapter
import ie.wit.my_festival.databinding.FragmentListBinding
import ie.wit.my_festival.main.FestivalApp

class ListFragment : Fragment() {

    lateinit var app: FestivalApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as FestivalApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_list)

        val layoutManager = LinearLayoutManager(requireContext())
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = FestivalAdapter(app.festivals.findAll(),null)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
