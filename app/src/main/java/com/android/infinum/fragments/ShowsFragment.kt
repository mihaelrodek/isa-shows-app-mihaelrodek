package com.android.infinum.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.R
import com.android.infinum.utils.ShowData.shows
import com.android.infinum.adapters.ShowsAdapter
import com.android.infinum.databinding.DialogUserBinding
import com.android.infinum.databinding.FragmentShowsBinding
import com.android.infinum.utils.showPermissionsDeniedSnackbar
import com.android.infinum.viewmodels.ShowsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File


class ShowsFragment : Fragment() {

    companion object {
        private const val SHARED_PREFS = "sharedPrefs"
        private const val USERNAME = "Username"
        private const val REMEMBERME = "RememberMe"
    }

    private var _binding: FragmentShowsBinding? = null
    private var _binding_dialog: DialogUserBinding? = null

    private val binding get() = _binding!!
    private val dialogbinding get() = _binding_dialog!!

    private var showsAdapter: ShowsAdapter? = null

    private val viewModel: ShowsViewModel by viewModels()

    private var user = ""
    private var rememberMe = false
    private lateinit var sharedPref : SharedPreferences

    var profilePhotoUri: Uri? = null

    private val cameraContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {

                Toast.makeText(context, "Uri is ${profilePhotoUri}", Toast.LENGTH_SHORT).show()

                Glide.with(this)
                    .load(profilePhotoUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.userPhoto)

                Glide.with(this)
                    .load(profilePhotoUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(dialogbinding.dialogProfilePhoto)
            }
        }

    private val cameraPermissionContract = preparePrmissionsContract({
        changeProfilePicture()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            this.requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit()
        user = sharedPreferences.getString(USERNAME, "").toString()
        rememberMe = sharedPreferences.getBoolean(REMEMBERME, false)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean("showSeen", rememberMe)
            apply()
        }

        initShowsRecycler()

        viewModel.getShowsLiveData().observe(requireActivity(), { shows ->
            updateItems()
        })

        binding.userPhoto.setOnClickListener {
            addProfileBottomSheet()
        }

        binding.refresh.setOnClickListener {
            binding.emptyStateLabel.isVisible = !binding.emptyStateLabel.isVisible
            binding.showsRecyclerView.isVisible = !binding.emptyStateLabel.isVisible
            //viewModel.initShows()
        }
    }

    private fun initShowsRecycler() {

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.showsRecyclerView.adapter = ShowsAdapter(shows) { item ->
            val action = ShowsFragmentDirections.actionSshowsToShowdetails(item.toInt())
            findNavController().navigate(action)
        }
    }

    private fun updateItems() {
        showsAdapter?.setItems(shows)
    }

    private fun addProfileBottomSheet() {
        val dialog = context?.let { BottomSheetDialog(it) }

        _binding_dialog = DialogUserBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogbinding.root)

        dialogbinding.dialogMail.text = user

        dialogbinding.dialogChangePhoto.setOnClickListener {
            cameraPermissionContract
        }

        dialogbinding.dialogLogout.setOnClickListener {
            dialog?.dismiss()
            with (sharedPref.edit()) {
                putBoolean("showSeen", false)
                putBoolean("reg", false)
                apply()
            }
            findNavController().navigate(R.id.action_shows_to_login)
        }

        dialog?.show()
    }

    private fun changeProfilePicture() {
        val filename = "profile_photo_capture"
        val imagePath = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val newFile = File(imagePath, filename)
        val authority = "com.android.infinum.fragments.fileprovider"

        profilePhotoUri = context?.let{
            FileProvider.getUriForFile(it, authority, newFile)
        }
        if (profilePhotoUri != null) {
            cameraContract. launch (profilePhotoUri)
        }
    }

    private fun preparePrmissionsContract(
        onPermissionsGranted: () -> Unit,
        onPermissionsDenied: () -> Unit = {
            activity?.let {
                showPermissionsDeniedSnackbar(it)
            }
        }
    ) = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied()
            }
        }
}