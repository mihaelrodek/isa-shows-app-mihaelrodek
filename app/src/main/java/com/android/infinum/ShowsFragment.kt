package com.android.infinum

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.infinum.ShowData.shows
import com.android.infinum.databinding.FragmentShowsBinding


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

        binding.refresh.setOnClickListener {
            binding.emptyStateLabel.isVisible = !binding.emptyStateLabel.isVisible
            binding.showsRecyclerView.isVisible = !binding.emptyStateLabel.isVisible
        }

        binding.logoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_second_to_first)
        }
    }

    private fun initShowsRecycler() {

        binding.showsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.showsRecyclerView.adapter = ShowsAdapter(shows) { item ->
            val action = ShowsFragmentDirections.actionSecondToThird(item.toInt())
            findNavController().navigate(action)
        }
        showsAdapter?.setItems(shows)
    }

}