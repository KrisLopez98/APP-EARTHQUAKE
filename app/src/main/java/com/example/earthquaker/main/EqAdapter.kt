 package com.example.earthquaker.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquaker.Earthqueake
import com.example.earthquaker.R
import com.example.earthquaker.databinding.EqListItemBinding

 private val TAG = EqAdapter::class.java.simpleName
 // Adminitra el RecyclerView
 // context: Context representa una propiedad y un argumento en el constructor
class EqAdapter(private val context: Context) : ListAdapter<Earthqueake, EqAdapter.EqViewHolder>(DiffCallback) {

     // Propiedades

    companion object DiffCallback : DiffUtil.ItemCallback<Earthqueake>(){
        override fun areItemsTheSame(oldItem: Earthqueake, newItem: Earthqueake): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Earthqueake, newItem: Earthqueake): Boolean {
            return oldItem.id== newItem.id
        }
    }

     // Propiedad representa una una funcion o un metodo
     private lateinit var onItemClickListener: ((earthquake: Earthqueake) -> Unit)

     fun setOnItemClickListener(onItemClickListener: (earthquake: Earthqueake) -> Unit){
         Log.d("clickItem", "EqAdapter")
         this.onItemClickListener = onItemClickListener
     }

    // Creacion de cada Item -> A cada terremoto
     // Crea la plantilla por cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EqAdapter.EqViewHolder {
        val binding = EqListItemBinding.inflate(LayoutInflater.from(parent.context))
        return EqViewHolder(binding)
    }

    // Asigna la informaciòn a la plantalla por cada elemento
     //HOLDER: EQVIEWMODEL
    override fun onBindViewHolder(holder: EqAdapter.EqViewHolder, position: Int) {
        val earthquake = getItem(position)
        holder.bind(earthquake)

    }

    // LOGICA DE UN ELEMENTO DE LA LISTA (RECYCLERVIEW)
    inner class EqViewHolder(private val binding: EqListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(earthqueake: Earthqueake){
            binding.eqMagnitudeText.text = context.getString(R.string.magnitude_format, earthqueake.magnitude)
            binding.eqPlaceText.text = earthqueake.place
            binding.root.setOnClickListener {
                Log.d("clickItem", "EqViewHolder")
                if (::onItemClickListener.isInitialized){
                onItemClickListener(earthqueake)
                }else{
                    Log.e(TAG, "onItemClickListener not initialized" )
                }
            }

        }
    }
}