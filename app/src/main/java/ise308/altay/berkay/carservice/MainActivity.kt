package ise308.altay.berkay.carservice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(){

    companion object {
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<FrameLayout>(R.id.frameLayoutID)
        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(container.id,homeFragment,null).show(homeFragment).commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_add_button, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbarAddButton->{
                Log.i(TAG,"Toolbar Add Button Clicked!")
                val intent = Intent(applicationContext,AddActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}
