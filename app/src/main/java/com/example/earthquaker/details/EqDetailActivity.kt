package com.example.earthquaker.details

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.earthquaker.Earthqueake
import com.example.earthquaker.R
import com.example.earthquaker.databinding.ActivityEqDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class EqDetailActivity : AppCompatActivity()  {
    companion object{
        const val EQ_KEY = "earthquake"

        private const val TIME_FORMAT = "dd/MMM/yyyy HH:mm:ss"
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val binding = ActivityEqDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("clickItem", "Detail")

        val earthquake = intent?.extras?.getParcelable<Earthqueake>(EQ_KEY)!!
        binding.magnitudeText.text = getString(R.string.magnitude_format, earthquake.magnitude)
        binding.longitudeText.text = earthquake.longitude.toString()
        binding.latitudeText.text = earthquake.latitude.toString()
        binding.placeText.text = earthquake.place
        setupTime(binding, earthquake)
    }

    private fun setupTime(binding: ActivityEqDetailBinding, earthqueake: Earthqueake){
        val dateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val date = Date(earthqueake.time)
        binding.timeText.text = dateFormat.format(date)
    }

}
