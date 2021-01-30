package ise308.altay.berkay.carservice

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity(){

    companion object {
        const val TAG = " AddActivity "
    }

    private lateinit var brandEditText : EditText
    private lateinit var modelEditText : EditText
    private lateinit var modelYearEditText:  EditText
    private lateinit var licensePlateEditText : EditText
    private lateinit var currentKMEditText : EditText
    private lateinit var lastServiceDatePickerButton : Button
    private lateinit var lastServiceKM : EditText
    private lateinit var selectedDate : String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        brandEditText  = findViewById(R.id.brandEditText)
        modelEditText  = findViewById(R.id.modelEditText)
        modelYearEditText  = findViewById(R.id.modelYearEditText)
        licensePlateEditText  = findViewById(R.id.licensePlateEditText)
        currentKMEditText  = findViewById(R.id.currentKMEditText)
        lastServiceDatePickerButton  = findViewById(R.id.lastServiceDateButton)
        lastServiceKM  = findViewById(R.id.lastServiceKMEditText)
        selectedDate=""


        lastServiceDatePickerButton.setOnClickListener{
            Log.i(TAG,"Date Picker Button Clicked!")
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
                if(monthOfYear+1<=9 && dayOfMonth<=9){
                    selectedDate = "$year-0${monthOfYear+1}-0$dayOfMonth"
                }else if(monthOfYear+1<=9){
                    selectedDate = "$year-0${monthOfYear+1}-$dayOfMonth"
                }else if(dayOfMonth+1<=9){
                    selectedDate = "$year-${monthOfYear+1}-0$dayOfMonth"
                }else{
                    selectedDate = "$year-${monthOfYear+1}-$dayOfMonth"
                }

                lastServiceDatePickerButton.text = selectedDate
            }, year, month, day)

            dpd.show()
        }

        findViewById<Button>(R.id.addNewCarButtonID).setOnClickListener{
            Log.i(TAG,"Add button clicked!")
            //check all informations. If there is not empty then save informations to the sqlite db.
            if(brandEditText.text.toString().isNotEmpty() && modelEditText.text.toString().isNotEmpty()
                && modelYearEditText.text.toString().isNotEmpty() && licensePlateEditText.text.toString().isNotEmpty()
                && currentKMEditText.text.toString().isNotEmpty() && selectedDate.isNotEmpty()
                && lastServiceKM.text.toString().isNotEmpty()){

                // get selected date string from button text and convert into the date format.
                val lastServiceDate = Date.valueOf(lastServiceDatePickerButton.text.toString())

                val sqLiteDB = SQLiteDB(applicationContext)
                sqLiteDB.addNewServiceRecord(Car(brandEditText.text.toString(),modelEditText.text.toString(),
                        modelYearEditText.text.toString().toInt(),licensePlateEditText.text.toString(),currentKMEditText.text.toString().toInt(),
                        lastServiceDate,lastServiceKM.text.toString().toInt()))
                finish()
            }else{
                //there is empty edit text or non selected date.
                Toast.makeText(applicationContext,"Please fill all informations!",Toast.LENGTH_LONG).show()
            }
        }
    }


}