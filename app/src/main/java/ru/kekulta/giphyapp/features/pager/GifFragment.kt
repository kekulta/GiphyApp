package ru.kekulta.giphyapp.features.pager

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentGifBinding
import ru.kekulta.giphyapp.databinding.FragmentListBinding
import ru.kekulta.giphyapp.shared.data.models.Gif

private const val ARG_GIF = "param1"


class GifFragment : Fragment(R.layout.fragment_gif) {
    private var gif: Gif? = null
    private val binding: FragmentGifBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gif = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_GIF, Gif::class.java)
            } else {
                it.getParcelable(ARG_GIF)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val circularProgressDrawable = CircularProgressDrawable(binding.image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        binding.image.transitionName = gif?.id + "hero"

        Glide.with(this)
            .load(
                gif?.url
            )
            .placeholder(circularProgressDrawable)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment!!.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    parentFragment!!.startPostponedEnterTransition()
                    return false
                }
            })
            .into(binding.image)
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(gif: Gif) =
            GifFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GIF, gif)
                }
            }
    }
}