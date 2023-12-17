package ie.wit.my_festival.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.squareup.picasso.Picasso
import ie.wit.my_festival.R
import ie.wit.my_festival.databinding.FragmentFestivalBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.fragment.findNavController
import ie.wit.my_festival.helpers.showImagePicker
import ie.wit.my_festival.main.FestivalApp
import ie.wit.my_festival.models.FestivalModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class FestivalFragment : Fragment() {

    lateinit var app: FestivalApp
    private var _fragBinding: FragmentFestivalBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    var festival = FestivalModel()
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as FestivalApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentFestivalBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.enter_festival_title)

        if (requireActivity().intent.hasExtra("festival_edit")) {
            edit = true
            festival = requireActivity().intent.extras?.getParcelable("festival_edit")!!
            fragBinding.festivalTitle.setText(festival.title)
            fragBinding.description.setText(festival.description)
            fragBinding.date.setText(festival.date)
            fragBinding.valueForMoney.rating = festival.valueForMoney
            fragBinding.accessibility.rating = festival.accessibility
            fragBinding.familyFriendly.rating = festival.familyFriendly
            fragBinding.btnAdd.setText(R.string.save_festival)
            Picasso.get()
                .load(festival.image)
                .into(fragBinding.festivalImage)
            if (festival.image != Uri.EMPTY) {
                fragBinding.chooseImage.setText(R.string.change_festival_image)
            }
        }

        setButtonListener(fragBinding)
        registerImagePickerCallback()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FestivalFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    private fun setButtonListener(layout: FragmentFestivalBinding) {
        layout.btnAdd.setOnClickListener {
            festival.title = layout.festivalTitle.text.toString()
            festival.description = layout.description.text.toString()
            festival.date = layout.date.text.toString()
            festival.valueForMoney = layout.valueForMoney.rating
            festival.accessibility = layout.accessibility.rating
            festival.familyFriendly = layout.familyFriendly.rating
            if (festival.title.isEmpty()) {
                Timber.i("Enter festival title")
            } else {
                if (edit) {
                    app.festivals.update(festival.copy())
                } else {
                    app.festivals.create(festival.copy())
                }
                // Return to ListFragment after saving
                findNavController().navigate(R.id.action_festivalFragment_to_listFragment)
            }

        }

        layout.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, requireContext())
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    android.app.Activity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            requireContext().contentResolver.takePersistableUriPermission(
                                image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            festival.image = image

                            Picasso.get()
                                .load(festival.image)
                                .into(fragBinding.festivalImage)
                            fragBinding.chooseImage.setText(R.string.change_festival_image)
                        }
                    }
                    android.app.Activity.RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_festival, menu)
        if (edit) menu.getItem(0).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
