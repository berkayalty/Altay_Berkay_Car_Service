package ise308.altay.berkay.carservice

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLOutput

class SQLiteDB(var context: Context):
    SQLiteOpenHelper(context, SQLiteDB.DB_NAME, null, SQLiteDB.DB_VERSION){

    val TABLE_NAME = "CarService"
    val ID = "id"
    val BRAND = "brand"
    val MODEL = "model"
    val MODEL_YEAR = "model_year"
    val LICENSE_PLATE = "license_plate"
    val CURRENT_KM = "current_km"
    val LAST_SERVICE_DATE= "last_service_date"
    val LAST_SERVICE_KM= "last_service_km"

    companion object{
        const val TAG = "SQLiteDB"
        private val DB_NAME = "SQLITE_DB"
        private val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?){
        Log.i(TAG,"Table Created.")
        val createTable = "CREATE TABLE $TABLE_NAME(" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$BRAND VARCHAR(50), $MODEL VARCHAR(50), " +
                "$MODEL_YEAR INTEGER,$LICENSE_PLATE VARCHAR(50), " +
                "$CURRENT_KM INTEGER, " +
                "$LAST_SERVICE_DATE DATE,$LAST_SERVICE_KM INTEGER)"
        db!!.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
    //get new car with parameter and save into sqlite db.
    fun addNewServiceRecord(car: Car){
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(BRAND, car.brand)
            put(MODEL, car.model)
            put(MODEL_YEAR, car.modelYear)
            put(LICENSE_PLATE, car.licensePlate)
            put(CURRENT_KM, car.currentKM)
            put(LAST_SERVICE_DATE, car.lastServiceDate!!.time)
            put(LAST_SERVICE_KM, car.lastServiceKM)
        }
        val newRowId = db?.insert(TABLE_NAME, null, values)
        Log.i(TAG,"New car inserted into table with id = $newRowId")
    }

    fun readAllServiceRecord(adapter : Adapter){
        Log.i(TAG,"Read all car service records from table.")
        //firstly clear all items from recycler view adapter.
        adapter.clearItems()
        val db =  this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast){
                var car = Car(cursor.getLong(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(BRAND)),
                        cursor.getString(cursor.getColumnIndex(MODEL)), cursor.getInt(cursor.getColumnIndex(MODEL_YEAR)),
                        cursor.getString(cursor.getColumnIndex(LICENSE_PLATE)), cursor.getInt(cursor.getColumnIndex(CURRENT_KM)),
                        cursor.getLong(cursor.getColumnIndex(LAST_SERVICE_DATE)), cursor.getInt(cursor.getColumnIndex(LAST_SERVICE_KM)))
                adapter.addItem(car)
                cursor.moveToNext()
            }
        }
    }

    fun updateCarServiceRecord(changedValuesMap : HashMap<String,Any>,changedCar : Car){
        Log.i(TAG,"Update car record with id = ${changedCar.ID}")
        val db = this.writableDatabase
        var queryStr = "UPDATE " + TABLE_NAME + " SET "
        var count = 0
        for(changedColumn in changedValuesMap){
            if(changedValuesMap.size-1 != count){
                queryStr += "${changedColumn.key} = '${changedColumn.value}',"
            }else{
                queryStr += "${changedColumn.key} = '${changedColumn.value}' WHERE ID = ${changedCar.ID}"
            }
            count++
        }
        Log.i(TAG,"Update Query String: $queryStr")
        db.execSQL(queryStr)
    }

    fun deleteCarService(car : Car){
        Log.i(TAG,"Deleted Car ID: ${car.ID}")
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME WHERE ID = ${car.ID}"
        db.execSQL(query)
    }

}