package ie.wit.my_festival.activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.my_festival.R
import ie.wit.my_festival.databinding.ActivityFestivalBinding
import ie.wit.my_festival.helpers.showImagePicker
import ie.wit.my_festival.main.FestivalApp
import ie.wit.my_festival.models.FestivalModel
import timber.log.Timber
import timber.log.Timber.Forest.i

class Festival : AppCompatActivity() {
    private lateinit var binding: ActivityFestivalBinding
    var festival = FestivalModel()
    lateinit var app: FestivalApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityFestivalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as FestivalApp

        if (intent.hasExtra("festival_edit")) {
            edit = true
            festival = intent.extras?.getParcelable("festival_edit")!!
            binding.festivalTitle.setText(festival.title)
            binding.description.setText(festival.description)
            binding.date.setText(festival.date)
            binding.valueForMoney.rating = festival.valueForMoney
            binding.accessibility.rating = festival.accessibility
            binding.familyFriendly.rating = festival.familyFriendly
            binding.btnAdd.setText(R.string.save_festival)
            Picasso.get()
                .load(festival.image)
                .into(binding.festivalImage)
            if (festival.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_festival_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            festival.title = binding.festivalTitle.text.toString()
            festival.description = binding.description.text.toString()
            festival.date = binding.date.text.toString()
            festival.valueForMoney = binding.valueForMoney.rating
            festival.accessibility = binding.accessibility.rating
            festival.familyFriendly = binding.familyFriendly.rating
            if (festival.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_festival_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.festivals.update(festival.copy())
                } else {
                    app.festivals.create(festival.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        registerImagePickerCallback()

    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            festival.image = image

                            Picasso.get()
                                .load(festival.image)
                                .into(binding.festivalImage)
                            binding.chooseImage.setText(R.string.change_festival_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_festival, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.festivals.delete(festival)
                finish()
            }        R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}