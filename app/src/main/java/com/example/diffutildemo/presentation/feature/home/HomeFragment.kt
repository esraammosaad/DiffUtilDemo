package com.example.diffutildemo.presentation.feature.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diffutildemo.R
import com.example.diffutildemo.Response
import com.example.diffutildemo.data.dto.CharacterDto
import com.example.diffutildemo.data.repository.CharacterRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), HomeListener {

    lateinit var adapter: RecyclerViewAdapter
    lateinit var progressBar: ProgressBar
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = HomeViewModelFactory(CharacterRepository())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        recyclerView = view.findViewById(R.id.charactersRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager

        adapter = RecyclerViewAdapter()
        adapter.listener = this
        recyclerView.adapter = adapter

        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.characters.collect { uiState ->
                    when (uiState) {
                        is Response.Loading -> {
                            handleLoadingState(true)
                        }

                        is Response.Success -> {
                            handleLoadingState(false)
                            handleSuccessState(uiState)
                        }

                        is Response.Failure -> {
                            handleFailureState(uiState)
                        }
                    }
                }
            }
        }
    }

    private fun handleSuccessState(uiState: Response.Success) {
        adapter.submitList(uiState.data as List<CharacterDto>)
    }

    private fun handleFailureState(uiState: Response.Failure) {
        Log.i("TAG", "handleFailureState: ${uiState.message}")
    }

    private fun handleLoadingState(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        recyclerView.isVisible = !isLoading
    }

    override fun onDeleteButtonClicked(character: CharacterDto) {
        homeViewModel.deleteCharacter(character)
        Snackbar.make(requireView(), getString(R.string.deleteSuccessMessage), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.undo)) {
                homeViewModel.addCharacter(character)
            }
            .show()
    }
}