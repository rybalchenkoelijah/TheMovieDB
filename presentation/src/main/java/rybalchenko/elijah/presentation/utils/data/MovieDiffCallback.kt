package rybalchenko.elijah.presentation.utils.data

import androidx.recyclerview.widget.DiffUtil
import rybalchenko.elijah.domain.entity.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
}