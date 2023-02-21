package com.hsb.mygalleryapp.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hsb.mygalleryapp.R
import com.hsb.mygalleryapp.Utils.fullPreview
import com.hsb.mygalleryapp.Utils.toast
import com.hsb.mygalleryapp.ViewModel.AppViewModel
import com.hsb.mygalleryapp.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottNavigation: BottomNavigationView
    private lateinit var navHost: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: AppViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mytoolbar)
        navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        bottNavigation = binding.bottomNav
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_folder, R.id.navigation_fav))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottNavigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
            val hideanim = AnimationUtils.loadAnimation(this, R.anim.hideanim)
            val unhide = AnimationUtils.loadAnimation(this, R.anim.unhideanim)
            val fade_out = AnimationUtils.loadAnimation(this, R.anim.exitfragment)
            val fade_in = AnimationUtils.loadAnimation(this, R.anim.openfragment)

            when (navDestination.id) {
                R.id.navigation_folder -> {
                    binding.cameraimg.animation = unhide
                    binding.cameraimg.isVisible = true
                    if (binding.deleteimg.isVisible) {
                        binding.deleteimg.animation = hideanim
                        binding.deleteimg.isVisible = false
                    }
                    if (!bottNavigation.isVisible){
                        bottNavigation.animation=unhide
                        bottNavigation.isVisible=true
                    }
                }
                R.id.navigation_preview -> {
                    if (bottNavigation.isVisible) {
                        bottNavigation.animation = hideanim
                        bottNavigation.isVisible = false
                    }
                    binding.deleteimg.animation = unhide
                    binding.deleteimg.isVisible = true
                    binding.cameraimg.animation = hideanim
                    binding.cameraimg.isVisible = false

                }
                R.id.navigation_gallery -> {
                    if (bottNavigation.isVisible) {
                        bottNavigation.animation = hideanim
                        bottNavigation.isEnabled = false
                        bottNavigation.isVisible = false
                    }
                    if (!binding.mytoolbar.isVisible) {
                        binding.mytoolbar.animation = fade_in
                        binding.mytoolbar.isVisible = true
                        binding.navHostFragment.requestLayout()
                    }
                    binding.deleteimg.animation = hideanim
                    binding.deleteimg.isVisible = false

                    binding.cameraimg.animation = hideanim
                    binding.cameraimg.isVisible = false

                }
                else -> {
                    if (!bottNavigation.isVisible) {
                        bottNavigation.animation = unhide
                        bottNavigation.isVisible = true
                        bottNavigation.isEnabled = true


                    }
                    if (!binding.mytoolbar.isVisible) {
                        binding.mytoolbar.animation = fade_in
                        binding.mytoolbar.isVisible = true
                        binding.navHostFragment.requestLayout()
                    }
                    binding.deleteimg.animation = hideanim
                    binding.deleteimg.isVisible = false
                    binding.cameraimg.animation = hideanim
                    binding.cameraimg.isVisible = false

                }
            }
        }

        fullPreview = {
            val fade_out = AnimationUtils.loadAnimation(this, R.anim.exitfragment)
            val fade_in = AnimationUtils.loadAnimation(this, R.anim.openfragment)

            if (binding.mytoolbar.isVisible) {
                binding.mytoolbar.animation = fade_out
                binding.mytoolbar.isVisible = false

            } else {

                binding.mytoolbar.animation = fade_in
                binding.mytoolbar.isVisible = true
            }
            binding.navHostFragment.requestLayout()
        }
        binding.deleteimg.setOnClickListener {
            viewModel.deleteAction?.invoke()

        }

        binding.cameraimg.setOnClickListener {
            toast("Take a picture")
            val pm: PackageManager = packageManager
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

            if (packages.toString().contains("camera")) {
                println("APPS: $packages")

            }
            val launchIntent = pm.getLaunchIntentForPackage("com.transsion.camera")
            startActivity(launchIntent)

        }

        transitionEffect()
    }

    private fun transitionEffect() {

    }


    override fun onSupportNavigateUp(): Boolean {
        return navHost.navController.navigateUp(appBarConfiguration)
    }
}