package com.android.infinum.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.R
import com.android.infinum.utils.ShowData.shows
import com.android.infinum.adapters.ShowsAdapter
import com.android.infinum.databinding.DialogUserBinding
import com.android.infinum.databinding.FragmentShowsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class ShowsFragment : Fragment() {

    companion object {
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private var showsAdapter: ShowsAdapter? = null

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

        initShowsRecycler()

        binding.userPhoto.setOnClickListener {
            addProfileBottomSheet()
        }

        binding.refresh.setOnClickListener {
            binding.emptyStateLabel.isVisible = !binding.emptyStateLabel.isVisible
            binding.showsRecyclerView.isVisible = !binding.emptyStateLabel.isVisible
        }
    }

    private fun initShowsRecycler() {

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.showsRecyclerView.adapter = ShowsAdapter(shows) { item ->
            val action =
                com.android.infinum.ShowsFragmentDirections.actionSecondToThird(item.toInt())
            findNavController().navigate(action)
        }
        showsAdapter?.setItems(shows)
    }

    private fun addProfileBottomSheet() {
        val dialog = context?.let { BottomSheetDialog(it) }

        val bottomSheetDialog = DialogUserBinding.inflate(layoutInflater)
        dialog?.setContentView(bottomSheetDialog.root)

        bottomSheetDialog.dialogChangePhoto.setOnClickListener {

        }

        bottomSheetDialog.dialogLogout.setOnClickListener {
            dialog?.dismiss()
            findNavController().navigate(R.id.action_second_to_first)
        }

        dialog?.show()
    }
}