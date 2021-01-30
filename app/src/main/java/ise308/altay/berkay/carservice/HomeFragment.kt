package ise308.altay.berkay.carservice

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment(){
    companion object {
        const val TAG = "HomeFragment"
    }
    private lateinit var recyclerView: RecyclerView
    lateinit var rcycAdapter : Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View?{
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewID)
        rcycAdapter = Adapter(requireContext(),this,requireActivity())
        recyclerView.adapter = rcycAdapter
        recyclerView.layoutManager  = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return view
    }

    override fun onResume(){
        super.onResume()
        Log.i(TAG,"HomeFragment OnResume!")
        val db = SQLiteDB(requireContext())
        db.readAllServiceRecord(rcycAdapter)
    }
}