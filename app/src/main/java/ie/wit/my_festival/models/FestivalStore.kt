package ie.wit.my_festival.models

import androidx.lifecycle.MutableLiveData


interface FestivalStore {
    fun findAll(): List<FestivalModel>

    fun findById(id: Long) : FestivalModel?
    fun create(festival: FestivalModel)
    fun update(festival: FestivalModel)
    fun delete(id: Long)
}