package com.udacity.asteroidradar.ui.detail


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        @Suppress("UNUSED_VARIABLE")
        val application = requireNotNull(activity).application
        val selectedAs = DetailFragmentArgs.fromBundle(arguments!!).selectedAsteroid

        val viewModelFactory = DetailViewModel.Factory(application, selectedAs)

        val binding = FragmentDetailBinding.inflate(inflater)
        binding.astoridModel = ViewModelProvider(
            this, viewModelFactory).get(DetailViewModel::class.java)

        binding.lifecycleOwner = this

        binding.detelis.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }

        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(activity!!)
            .setMessage(getString(R.string.astronomical_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
