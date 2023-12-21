package ie.wit.my_festival.models

import timber.log.Timber.Forest.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object FestivalManager : FestivalStore {

    val festivals = ArrayList<FestivalModel>()

    override fun findAll(): List<FestivalModel> {
        return festivals
    }

    override fun findById(id:Long) : FestivalModel? {
        val foundFestival: FestivalModel? = festivals.find { it.id == id }
        return foundFestival
    }

    override fun create(festival:FestivalModel) {
        festival.id = getId()
        festivals.add(festival)
        logAll()
    }

    override fun update(festival: FestivalModel) {
        var foundFestival: FestivalModel? = festivals.find { p -> p.id == festival.id }
        if (foundFestival != null) {
            foundFestival.title = festival.title
            foundFestival.description = festival.description
            foundFestival.date = festival.date
            foundFestival.valueForMoney = festival.valueForMoney
            foundFestival.accessibility = festival.accessibility
            foundFestival.familyFriendliness = festival.familyFriendliness
            foundFestival.image = festival.image
            logAll()
        }
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
    fun logAll() {
        festivals.forEach{ i("${it}") }
    }
}