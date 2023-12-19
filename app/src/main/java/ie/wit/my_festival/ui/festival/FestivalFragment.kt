package ie.wit.my_festival.ui.festival

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import ie.wit.my_festival.R
import ie.wit.my_festival.databinding.FragmentFestivalBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.my_festival.helpers.showImagePicker
import ie.wit.my_festival.models.FestivalModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class FestivalFragment : Fragment() {


    private var _fragBinding: FragmentFestivalBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: FestivalViewModel

    var festival = FestivalModel()
    var edit = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentFestivalBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        viewModel = ViewModelProvider(this).get(FestivalViewModel::class.java)
        activity?.title = getString(R.string.enter_festival_title)
        setupMenu()

        if (arguments?.containsKey("festival_edit") == true) {
            edit = true
            festival = arguments?.getParcelable("festival_edit") ?: FestivalModel()

            viewModel.setFestival(festival)
            viewModel.festival.observe(viewLifecycleOwner, { festival ->
                fragBinding.festivalTitle.setText(festival.title)
                fragBinding.description.setText(festival.description)
                fragBinding.date.setText(festival.date)
                fragBinding.valueForMoney.rating = festival.valueForMoney
                fragBinding.accessibility.rating = festival.accessibility
                fragBinding.familyFriendly.rating = festival.familyFriendly
                fragBinding.btnAdd.setText(R.string.save_festival)
                Picasso.get().load(festival.image).into(fragBinding.festivalImage)
                if (festival.image != Uri.EMPTY) {
                    fragBinding.chooseImage.setText(R.string.change_festival_image)
                }
            })
        }

        setButtonListener(fragBinding)
        registerImagePickerCallback()

        return root
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
                    viewModel.updateFestival(festival.copy())
                } else {
                    viewModel.createFestival(festival.copy())
                }
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

                            Picasso.get().load(festival.image).into(fragBinding.festivalImage)
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

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_festival, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}