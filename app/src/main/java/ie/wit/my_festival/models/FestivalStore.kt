package ie.wit.my_festival.models


interface FestivalStore {
    fun findAll(): List<FestivalModel>
    fun findById(id: Long) : FestivalModel?
    fun create(festival: FestivalModel)
    fun update(festival: FestivalModel)
    fun delete(festival: FestivalModel)
}