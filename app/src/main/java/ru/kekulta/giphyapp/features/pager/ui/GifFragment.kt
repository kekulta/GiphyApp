package ru.kekulta.giphyapp.features.pager.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentGifBinding
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.goodjobray.shared.data.utils.dp

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
        circularProgressDrawable.strokeWidth = 5f.dp
        circularProgressDrawable.centerRadius = 30f.dp
        circularProgressDrawable.start()

        Glide.with(this)
            .load(
                gif?.urlDownsized ?: gif?.urlOriginal
            )
            .placeholder(circularProgressDrawable)
            // TODO add error placeholder
            .error(R.drawable.ic_launcher_background)
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