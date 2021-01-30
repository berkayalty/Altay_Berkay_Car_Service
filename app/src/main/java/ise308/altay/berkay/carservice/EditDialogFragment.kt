package ise308.altay.berkay.carservice

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.sql.Date
import java.util.*
import kotlin.collections.HashMap


class EditDialogFragment(var car : Car,var homeFragment: HomeFragment) : DialogFragment(){

    companion object {
        const val TAG = "EditDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        val builder = AlertDialog.Builder(requireActivity())

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_edit,null)
        builder.setView(view)
                .setMessage("Edit Car Service Record!")

        val editBrand = view.findViewById<EditText>(R.id.editBrand)
        editBrand.setText(car.brand)
        val editModel = view.findViewById<EditText>(R.id.editModel)
        editModel.setText(car.model)
        val editModelYear = view.findViewById<EditText>(R.id.editModelYear)
        editModelYear.setText(car.modelYear!!.toString())
        val editLicensePlate = view.findViewById<EditText>(R.id.editLicensePlate)
        editLicensePlate.setText(car.licensePlate)
        val editCurrentKM = view.findViewById<EditText>(R.id.editCurrentKM)
        editCurrentKM.setText(car.currentKM!!.toString())
        val editLastServiceKM = view.findViewById<EditText>(R.id.editLastServiceKM)
        editLastServiceKM.setText(car.lastServiceKM!!.toString())

        val lastServiceDateButton2 = view.findViewById<Button>(R.id.lastServiceDateButton2)
        lastServiceDateButton2.text = car.lastServiceDate.toString()
        var selectedDate = car.lastServiceDate.toString()
        lastServiceDateButton2.setOnClickListener {
            Log.i(TAG,"Date Picker Dialog opened from edit dialog fragment")
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                if(monthOfYear+1<=9 && dayOfMonth<=9){
                    selectedDate = "$year-0${monthOfYear+1}-0$dayOfMonth"
                }else if(monthOfYear+1<=9){
                    selectedDate = "$year-0${monthOfYear+1}-$dayOfMonth"
                }else if(dayOfMonth+1<=9){
                    selectedDate = "$year-${monthOfYear+1}-0$dayOfMonth"
                }else{
                    selectedDate = "$year-${monthOfYear+1}-$dayOfMonth"
                }
                lastServiceDateButton2.text = selectedDate
            }, year, month, day)

            dpd.show()
        }


        view.findViewById<Button>(R.id.editCarButton).setOnClickListener{
            Log.i(TAG,"Edit Car Button Clicked!")
            //first of all check are there any empty edit text or date.
            if(car.brand != editBrand.text.toString() || car.model != editModel.text.toString()
                    || car.modelYear != editModelYear.text.toString().toInt() || car.licensePlate != editLicensePlate.text.toString()
                    || car.currentKM != editCurrentKM.text.toString().toInt() || car.lastServiceKM != editLastServiceKM.text.toString().toInt()
                    || selectedDate != car.lastServiceDate.toString()){

                        val db = SQLiteDB(requireContext())
                        val changedValuesMap : HashMap<String,Any> = HashMap()
                        //then find the car's changed values based on previous values and saved them into hashmap:(COLUMN_NAME,CHANGED_VALUE)
                        if(car.brand != editBrand.text.toString()){
                            changedValuesMap.put(db.BRAND,editBrand.text.toString())
                        }
                        if(car.model != editModel.text.toString()){
                            changedValuesMap.put(db.MODEL,editModel.text.toString())
                        }
                        if(car.modelYear != editModelYear.text.toString().toInt()){
                            changedValuesMap.put(db.MODEL_YEAR,editModelYear.text.toString().toInt())
                        }
                        if(car.licensePlate != editLicensePlate.text.toString()){
                            changedValuesMap.put(db.LICENSE_PLATE,editLicensePlate.text.toString())
                        }
                        if(car.currentKM != editCurrentKM.text.toString().toInt()){
                            changedValuesMap.put(db.CURRENT_KM,editCurrentKM.text.toString().toInt())
                        }
                        if(car.lastServiceKM != editLastServiceKM.text.toString().toInt()){
                            changedValuesMap.put(db.LAST_SERVICE_KM,editLastServiceKM.text.toString().toInt())
                        }
                        if(car.lastServiceDate.toString() != selectedDate){
                            val date = Date.valueOf(selectedDate)
                            changedValuesMap.put(db.LAST_SERVICE_DATE,date.time)
                        }
                        //call sqlite db helper classes update function with hashmap parameter and changed car
                        db.updateCarServiceRecord(changedValuesMap,this.car)
                        homeFragment.onResume()
                        dismiss()
            }else{
                Toast.makeText(requireContext(),"There is no changed value!",Toast.LENGTH_LONG).show()
            }

        }
        return builder.create()
    }
}