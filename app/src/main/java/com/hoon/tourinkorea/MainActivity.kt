package com.hoon.tourinkorea

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hoon.tourinkorea.databinding.ActivityMainBinding
import com.hoon.tourinkorea.ui.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view)?.findNavController()
        navController?.let {
            binding.bottomNav.setupWithNavController(it)
        }

//        binding.toolbarHome.setNavigationOnClickListener {
//            binding.drawerLayout.openDrawer(binding.navigationView)
//        }

        binding.writeButton.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }
    }
}