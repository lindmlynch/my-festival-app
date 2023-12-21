package ie.wit.my_festival.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.my_festival.models.FestivalManager
import ie.wit.my_festival.models.FestivalModel

class FestivalDetailViewModel : ViewModel() {
    private val festival = MutableLiveData<FestivalModel>()

    val observableFestival: LiveData<FestivalModel>
        get() = festival

    fun getFestival(id: Long) {
        festival.value = FestivalManager.findById(id)
    }
}