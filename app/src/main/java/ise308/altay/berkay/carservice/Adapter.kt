package ise308.altay.berkay.carservice

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.SQLOutput


class Adapter internal constructor(var context: Context,var homeFragment : HomeFragment,var activity : Activity) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    companion object {
        const val TAG = "Adapter"
    }

    private val data: ArrayList<Car>
    private val mInflater: LayoutInflater

    init{
        mInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        data = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addItem(car: Car){
        Log.i(TAG,"Car ${car.ID} rendered.")
        data.add(car)
        this.notifyDataSetChanged()
    }
    fun clearItems(){
        this.data.clear()
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val editButton : ImageButton
        private val deleteButton : ImageButton
        private val licensePlate : TextView
        private val brand : TextView
        private val model : TextView
        private lateinit var car : Car

        val editDeleteViewOnClick = View.OnClickListener {
            when(it.tag){
                "EDIT_BUTTON_TAG" ->{
                    //open edit dialog fragment
                    val editDialog = EditDialogFragment(this.car,homeFragment)
                    editDialog.show((activity as MainActivity).supportFragmentManager,null)
                }
                "DELETE_BUTTON_TAG" ->{
                    // show default alert dialog for confirm the delete operation.
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                    alertDialogBuilder.setTitle("Delete Service Record!")
                    alertDialogBuilder.setMessage("Are you sure for delete?")
                    alertDialogBuilder.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
                        val db = SQLiteDB(context)
                        db.deleteCarService(car)
                        homeFragment.onResume()
                    })
                    alertDialogBuilder.setNegativeButton("No",DialogInterface.OnClickListener  { dialog, which ->
                        dialog.dismiss()
                    })
                    alertDialogBuilder.create().show()

                }
            }
        }

        init{
            // initialize views.
            editButton = itemView.findViewById(R.id.editButton)
            editButton.setOnClickListener(editDeleteViewOnClick)
            editButton.tag="EDIT_BUTTON_TAG"
            deleteButton = itemView.findViewById(R.id.deleteButton)
            deleteButton.setOnClickListener(editDeleteViewOnClick)
            deleteButton.tag="DELETE_BUTTON_TAG"
            licensePlate = itemView.findViewById(R.id.licensePlateTextView)
            brand = itemView.findViewById(R.id.brandTextView)
            model = itemView.findViewById(R.id.modelTextView)
        }

        fun setData(car: Car){
            this.car = car
            licensePlate.text = car.licensePlate
            brand.text = car.brand
            model.text = car.model
            if(car.isNeedService!!){
                itemView.setBackgroundColor(Color.RED)
            }else{
                itemView.setBackgroundColor(Color.GREEN)
            }
        }

    }


}