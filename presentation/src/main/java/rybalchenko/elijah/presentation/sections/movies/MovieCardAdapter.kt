package rybalchenko.elijah.presentation.sections.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.presentation.utils.glide.ImageLoader
import rybalchenko.elijah.test.R
import rybalchenko.elijah.test.databinding.MovieCardItemBinding
import javax.inject.Inject

class MovieCardAdapter @Inject constructor(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val imageLoader: ImageLoader
) :
    PagedListAdapter<Movie, MovieCardAdapter.MovieCardViewHolder>(diffCallback) {

    var clickListener: ((Movie) -> Unit)? = null
    var shareListener: ((Movie) -> Unit)? = null

    class MovieCardViewHolder(
        itemView: View, private val imageLoader: ImageLoader,
        private val clickListener: ((Movie) -> Unit)?,
        private val shareListener: ((Movie) -> Unit)?
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = MovieCardItemBinding.bind(itemView)

        fun bind(movie: Movie) {
            binding.movie = movie
            binding.addRemoveBT.setOnClickListener { clickListener?.invoke(movie) }
            binding.shareBT.setOnClickListener { shareListener?.invoke(movie)}
            imageLoader.loadImage(movie.posterPath, binding.posterIV)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCardViewHolder {
        return MovieCardViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_card_item, parent, false),
            imageLoader,
            clickListener,
            shareListener
        )
    }

    override fun onBindViewHolder(holder: MovieCardViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.bind(item)
        }
    }
}