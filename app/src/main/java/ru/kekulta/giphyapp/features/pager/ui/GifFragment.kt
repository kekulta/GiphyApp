package ru.kekulta.giphyapp.features.pager.ui

import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.FragmentGifBinding
import ru.kekulta.giphyapp.features.pager.domain.presentation.GifViewModel
import ru.kekulta.giphyapp.shared.data.models.Gif
import ru.kekulta.goodjobray.shared.data.utils.dp
import java.io.*


private const val ARG_GIF = "param1"

class GifFragment : Fragment(R.layout.fragment_gif) {
    private var gif: Gif? = null
    private val binding: FragmentGifBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val viewModel: GifViewModel by viewModels({ requireActivity() }) { GifViewModel.Factory }

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
        //TODO set tint
        circularProgressDrawable.start()

        Glide.with(this)
            .load(
                gif?.urlDownsized ?: gif?.urlOriginal
            )
            .override(gif?.width ?: 100, gif?.height ?: 150)
            .placeholder(circularProgressDrawable)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Do nothing
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    binding.gifShareButton.setOnClickListener {
                        lifecycleScope.launch(Dispatchers.IO) {


                            val gifFile =
                                Glide.with(requireContext()).asFile()
                                    .load(gif?.urlDownsized ?: gif?.urlOriginal)
                                    .submit().get()


                            val uri: Uri =
                                getUriForFile(
                                    requireContext(),
                                    "ru.kekulta.provider",
                                    gifFile,
                                    "${gif?.title ?: "giphy"}.gif"
                                )


                            val shareIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_STREAM, uri)
                                type = "video/gif"
                            }
                            startActivity(Intent.createChooser(shareIntent, null))

                        }
                    }

                    binding.gifShareButton.isVisible = true
                    return false
                }

            })
            // TODO add error placeholder
            .error(R.drawable.ic_launcher_background)
            .into(binding.image)

        gif?.let {
            if (it.user?.name != null) {
                binding.gifUserTv.text = it.user.name
                Glide.with(binding.gifUserTv.context)
                    .asBitmap()
                    .load(it.user.avatar)
                    .circleCrop()
                    .into(object : CustomTarget<Bitmap?>(40f.dp.toInt(), 40f.dp.toInt()) {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            try {
                                binding.gifUserTv.setCompoundDrawablesWithIntrinsicBounds(
                                    BitmapDrawable(binding.gifUserTv.resources, resource),
                                    null,
                                    null,
                                    null
                                )
                            } catch (e: java.lang.IllegalStateException) {
                                Log.e(
                                    LOG_TAG,
                                    "Tried to set compound drawable to currently removed page",
                                    e
                                )
                            }

                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            //Do nothing
                        }
                    })
                binding.gifUserTv.isVisible = true
            }
            if (it.title != null) {
                binding.gifTitleTv.text = it.title
                binding.gifTitleTv.isVisible = true
            }
            binding.image.contentDescription = it.contentDescription

            val text = ((it.urlOriginal ?: it.urlDownsized) ?: it.urlPreview)
            if (text != null) {
                binding.gifLinkButton.isVisible = true

                // TODO change color of toast
                binding.gifLinkButton.setOnClickListener {
                    val clipboard =
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Link to gif", text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(requireContext(), "Link copied!", Toast.LENGTH_SHORT).show()
                }
            }

            gif?.let { gif ->
                binding.gifLikeButton.isChecked = gif.liked
                binding.gifLikeButton.setOnClickListener {
                    viewModel.changeLike(gif.id)
                }
            }


        }



        return binding.root
    }

    companion object {
        const val LOG_TAG = "GifFragment"

        @JvmStatic
        fun newInstance(gif: Gif) =
            GifFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_GIF, gif)
                }
            }
    }
}