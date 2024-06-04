package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.repo.AsteroidsRepository
import com.udacity.asteroidradar.ui.AsteroidRcListener


class MainFragment: Fragment(), AsteroidRcListener {
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)  {
            " wait for create viewmodel"
        }

        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    private lateinit var listener: AsteroidRcListener
    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val binding: FragmentMainBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        listener = this
        adapter = AsteroidAdapter(listener)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.rcAstroid.adapter = this.adapter

        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            asteroids?.let {
                adapter.submitList(it)
            }
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, {
            Log.i("MainFragment", "Picture of day object fetched. Media type: ${it.mediaType}")
        })

        viewModel.selectedAs.observe(viewLifecycleOwner, { selectedAsteroid ->
            selectedAsteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.showDetailFragmentComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_past_week_menu -> viewModel.getLiveData(AsteroidsRepository.DataFilter.Get_PAST_WEEK)
            R.id.show_all_menu -> viewModel.getLiveData()
            R.id.show_today_menu -> viewModel.getLiveData(AsteroidsRepository.DataFilter.SHOW_TODAY)

        }

        return true
    }

    // adapter interface method implementation
    override fun onItemViewclick(asteroid: Asteroid) {
        viewModel.showDetailFragment(asteroid)
    }
}


