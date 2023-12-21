package ie.wit.my_festival.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.my_festival.databinding.CardFestivalBinding
import ie.wit.my_festival.models.FestivalModel

interface FestivalListener {
    fun onFestivalClick(festival: FestivalModel, position : Int)
}

class FestivalAdapter constructor(
    private var festivals: ArrayList<FestivalModel>,
    private val listener: FestivalListener? = null
) : RecyclerView.Adapter<FestivalAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFestivalBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val festival = festivals[position]
        holder.bind(festival, listener)
    }

    override fun getItemCount(): Int = festivals.size

    fun removeAt(position: Int) {
        festivals.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder(private val binding: CardFestivalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(festival: FestivalModel, listener: FestivalListener?) {
            binding.festivalTitle.text = festival.title
            binding.description.text = festival.description
            binding.date.text = festival.date
            binding.valueForMoney.rating = festival.valueForMoney
            binding.accessibility.rating = festival.accessibility
            binding.familyFriendliness.rating = festival.familyFriendliness
            Picasso.get().load(festival.image).resize(200, 200).into(binding.imageIcon)

            binding.root.setOnClickListener {
                listener?.onFestivalClick(festival, adapterPosition)
            }
        }
    }
}
