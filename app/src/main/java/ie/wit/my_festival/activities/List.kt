package ie.wit.my_festival.activities



import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.my_festival.R
import ie.wit.my_festival.adapters.FestivalAdapter
import ie.wit.my_festival.adapters.FestivalListener
import ie.wit.my_festival.databinding.ActivityListBinding
import ie.wit.my_festival.main.FestivalApp
import ie.wit.my_festival.models.FestivalModel


class List : AppCompatActivity(), FestivalListener {

    lateinit var app: FestivalApp
    private lateinit var binding: ActivityListBinding
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as FestivalApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = FestivalAdapter(app.festivals.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_festival -> {
                val launcherIntent = Intent(this, Festival::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.festivals.findAll().size)
            }
        }

    override fun onFestivalClick(festival: FestivalModel, pos : Int) {
        val launcherIntent = Intent(this, Festival::class.java)
        launcherIntent.putExtra("festival_edit", festival)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.festivals.findAll().size)
            }
            else // Deleting
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }
}