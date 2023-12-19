package ie.wit.my_festival.ui.list


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.my_festival.models.FestivalManager
import ie.wit.my_festival.models.FestivalModel

class ListViewModel : ViewModel() {

    private val festivalsList = MutableLiveData<List<FestivalModel>>()

    val observableFestivalsList: LiveData<List<FestivalModel>>
        get() = festivalsList

    init {
        load()
    }

    fun load() {
        festivalsList.value = FestivalManager.findAll()
    }
}
