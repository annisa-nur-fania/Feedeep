package com.dicoding.picodiploma.feedeepaplikasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import com.dicoding.picodiploma.feedeepaplikasi.Data.FeedeepData
import com.dicoding.picodiploma.feedeepaplikasi.Detail.DetailActivity
import com.dicoding.picodiploma.feedeepaplikasi.Home.MainAccessCamera
import com.dicoding.picodiploma.feedeepaplikasi.UI.About
import com.dicoding.picodiploma.feedeepaplikasi.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainModel : DetailActivity
    private val itemdetect = ArrayList<FeedeepData>()
    private lateinit var adapter: FeedeepAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Feedeep"

        //intentkeakseskamera
        binding.picture.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainAccessCamera::class.java))

        }
    }override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }


    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_about -> {
                showAbout()
            }
        }
    }

    private fun showAbout() {
        val moveIntent = Intent(this@MainActivity, About::class.java)
        startActivity(moveIntent)
    }
}