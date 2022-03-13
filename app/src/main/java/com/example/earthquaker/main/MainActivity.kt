package com.example.earthquaker.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earthquaker.Earthqueake
import com.example.earthquaker.R
import com.example.earthquaker.api.ApiResponseStatus
import com.example.earthquaker.api.WorkerUtil
import com.example.earthquaker.databinding.ActivityMainBinding
import com.example.earthquaker.details.EqDetailActivity


private const val SORT_TYPE_KEY = "sort_type"
// View
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)

        WorkerUtil.scheduleSync(this)
        val sortTye = getSortType()

        viewModel = ViewModelProvider(this,
            MainViewModelFactory(application, sortTye)).get(MainViewModel::class.java)


        // Uso del constructor
        // Creando un objeto
        val adapter = EqAdapter(this)
        binding.eqRecycler.adapter = adapter

        adapter.setOnItemClickListener{
            Log.d("clickItem", "EJECUTA LISTENER")
            openDetailActivity(it)
        }



        viewModel.eqList.observe(this, Observer { eqList : MutableList<Earthqueake> ->
            // Asinar la lista o la colecciòn de objetos que se mostraran en pantalla
            adapter.submitList(eqList)

            handleEmptyView(eqList, binding)
        })



        viewModel.status.observe(this, Observer {
            apiResponseStatus : ApiResponseStatus ->
            if (apiResponseStatus == ApiResponseStatus.LOADING){
                binding.loadingWheel.visibility = View.VISIBLE
            }else if (apiResponseStatus == ApiResponseStatus.DONE){
                binding.loadingWheel.visibility = View.GONE
            }else if (apiResponseStatus == ApiResponseStatus.ERROR){
                binding.loadingWheel.visibility = View.GONE
            }
        })
    }

    private fun getSortType(): Boolean {
        val prefs = getPreferences(MODE_PRIVATE)
        return prefs.getBoolean(SORT_TYPE_KEY, false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.main_menu_sort_magnitude){
            viewModel.reloadEarthquakesFromDataBase(true)
            saveSortType(true)
        } else if (itemId == R.id.main_menu_sort_time){
            viewModel.reloadEarthquakesFromDataBase(false)
            saveSortType(false)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveSortType(sortByMagnitude: Boolean){
        val prefs = getPreferences(MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(SORT_TYPE_KEY, sortByMagnitude)
        editor.apply()
    }

    private fun handleEmptyView(
        eqList: MutableList<Earthqueake>,
        binding: ActivityMainBinding
    ) {
        if (eqList.isEmpty()) {
            binding.eqEmptyView.visibility = View.VISIBLE
        } else {
            binding.eqEmptyView.visibility = View.GONE
        }
    }

    private fun openDetailActivity(earthqueake: Earthqueake){
        Log.d("clickItem", "Entra al metodo open detail")
        val intent = Intent(this, EqDetailActivity::class.java)
        intent.putExtra(EqDetailActivity.EQ_KEY, earthqueake)
        startActivity(intent)


    }
}