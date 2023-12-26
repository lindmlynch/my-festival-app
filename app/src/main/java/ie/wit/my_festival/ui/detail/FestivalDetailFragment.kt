package ie.wit.my_festival.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.my_festival.databinding.FragmentFestivalDetailBinding
import ie.wit.my_festival.ui.auth.LoggedInViewModel
import ie.wit.my_festival.ui.list.ListViewModel
import timber.log.Timber


class FestivalDetailFragment : Fragment() {

    private lateinit var detailViewModel: FestivalDetailViewModel
    private val args by navArgs<FestivalDetailFragmentArgs>()
    private var _fragBinding: FragmentFestivalDetailBinding? = null
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val listViewModel : ListViewModel by activityViewModels()
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentFestivalDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(FestivalDetailViewModel::class.java)
        detailViewModel.observableFestival.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editFestivalButton.setOnClickListener {
            detailViewModel.updateFestival(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.festivalid, fragBinding.festivalvm?.observableFestival!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteFestivalButton.setOnClickListener {
            listViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableFestival.value?.uid!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
        val currentFestival = detailViewModel.observableFestival.value ?: return

        fragBinding.festivalTitle.setText(currentFestival.title)
        fragBinding.description.setText(currentFestival.description)
        fragBinding.date.setText(currentFestival.date)

        fragBinding.valueForMoneyEditable.rating = currentFestival.valueForMoney
        fragBinding.accessibilityEditable.rating = currentFestival.accessibility
        fragBinding.familyFriendlinessEditable.rating = currentFestival.familyFriendliness
        fragBinding.festivalvm = detailViewModel

        Timber.i("Festival data updated in UI")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getFestival(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.festivalid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}