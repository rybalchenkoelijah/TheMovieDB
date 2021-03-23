package rybalchenko.elijah.presentation.utils.glide

import android.widget.ImageView
import com.bumptech.glide.Glide
import rybalchenko.elijah.test.R

class ImageLoader (private val photoUrl: String) {
    fun loadImage(path: String, target: ImageView) {
        Glide.with(target.context)
            .load(photoUrl + path)
            .error(R.drawable.ic_image_not_found)
            .into(target)
    }
}