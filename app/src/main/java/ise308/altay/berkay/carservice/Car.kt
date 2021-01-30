package ise308.altay.berkay.carservice
import android.util.Log
import java.sql.Date
import java.sql.Timestamp

class Car{

    companion object {
        const val TAG = " Car "
    }
    var ID : Long ?= null
    var brand : String ?= null
    var model : String ?= null
    var modelYear : Int ?= null
    var licensePlate : String ?= null
    var currentKM : Int ?= null
    var lastServiceDate : Date ?= null
    var lastServiceKM : Int ?= null
    var isNeedService : Boolean ?= null

    //this constructor works when reading from database
    constructor(ID: Long?, brand: String?, model: String?, modelYear: Int?, licensePlate: String?,
                currentKM: Int?, lastServiceDate: Long?, lastServiceKM: Int?){
        Log.i(TAG,"Car ID: $ID constructor.")
        this.ID = ID
        this.brand = brand
        this.model = model
        this.modelYear = modelYear
        this.licensePlate = licensePlate
        this.currentKM = currentKM
        this.lastServiceDate = Date(lastServiceDate!!)
        this.lastServiceKM = lastServiceKM
        //read current date in timestamp format
        val currentDateTimeStamp = Date(System.currentTimeMillis()).time
        //convert date format into timestamp.
        val lastServiceTimestamp = this.lastServiceDate!!.time
        //if the cars current km more than 20.000 from last service km OR
        //if there is 2 years between current service date and last service date then car needs to goto service.
        //otherwise isNeedService assigned false and item background setted red.
        this.isNeedService = (((currentDateTimeStamp - lastServiceTimestamp)/ (1000 * 60 * 60 * 24 * 365) >= 2) || (currentKM!! - lastServiceKM!! >= 20000))
    }

    //program use this constructor when new car service record created and saved to database
    constructor(
        brand: String?,
        model: String?,
        modelYear: Int?,
        licensePlate: String?,
        currentKM: Int?,
        lastServiceDate: Date?,
        lastServiceKM: Int?
    ){
        this.brand = brand
        this.model = model
        this.modelYear = modelYear
        this.licensePlate = licensePlate
        this.currentKM = currentKM
        this.lastServiceDate = lastServiceDate
        this.lastServiceKM = lastServiceKM
        this.isNeedService = isNeedService
    }


}