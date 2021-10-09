package com.example.carnot


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.carnot.ResponsePojoClass.DRecords
import com.example.carnot.ResponsePojoClass.DistrictWise
import com.example.carnot.ResponsePojoClass.Records
import com.example.carnot.ResponsePojoClass.VillageWise
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var isVillageSelected = true
    var isDateSelected = false


    var myVillageList = ArrayList<Records>()
    var myDistrictList = ArrayList<DRecords>()

    var myVillageListFiltered = ArrayList<Records>()
    var myDistrictListFiltered = ArrayList<DRecords>()

    var lv: ListView? = null

    var progressBar: ProgressBar?= null


     var spinner: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)



        lv = findViewById(R.id.mainlist) as ListView

        spinner = findViewById(R.id.progressBar1) as ProgressBar
        spinner!!.setVisibility(View.GONE);

        villagewisebutton.setOnClickListener(View.OnClickListener {
            VillageWiseAPIService()
            isVillageSelected = true
            villagewisebutton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            districtwisebutton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey))
        })

        districtwisebutton.setOnClickListener(View.OnClickListener {
            DistrictWiseAPIService()
            isVillageSelected = false
            districtwisebutton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            villagewisebutton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.grey))
        })

        pricewisebutton.setOnClickListener(View.OnClickListener {
            filtertext.visibility = View.VISIBLE
            filtertext.setText("")
            isDateSelected = false
        })

        datewisebutton.setOnClickListener(View.OnClickListener {
            filtertext.visibility = View.VISIBLE
            filtertext.setText("")
            isDateSelected = true



            ShowDatePicker();


        })

        filtertext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

//                if(isDateSelected)
//                {
//                   //MyVillageBaseAdapter(this@MainActivity,myVillageList).filter(s.toString(),"2")//
//                }

                if(isDateSelected)
                {
                    filter(s.toString(),"2")
                }
                else
                {
                    filter(s.toString(),"1")
                }


            }
        })

        VillageWiseAPIService()



    }



    fun VillageWiseAPIService() {

        showProgress()
        myVillageList.clear();

        val service = Retrofit.Builder()
            .baseUrl("https://api.data.gov.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java);

        service.getVillageWiseData().enqueue(object : Callback<VillageWise> {

            /* The HTTP call failed. This method is run on the main thread */
            override fun onFailure(call: Call<VillageWise>, t: Throwable) {
                Log.d("TAG_ V", "An error happened!")
                dismissProgress()
                t.printStackTrace()

            }

            /* The HTTP call was successful, we should still check status code and response body
             * on a production app. This method is run on the main thread */
            override fun onResponse(call: Call<VillageWise>, response: Response<VillageWise>) {
                /* This will print the response of the network call to the Logcat */


                    dismissProgress()
                Log.d("TAG_ V 1", response.message())



                response.body()?.toString()?.let { Log.d("TAG_ V 2", it) }


                for(item in response.body()?.records!!)
                {
                    myVillageList.add(item)
                }



                filtertext.setText("")

                lv?.adapter = MyVillageBaseAdapter(this@MainActivity,myVillageList);





            }
        })


    }





    fun DistrictWiseAPIService() {


        showProgress()
        myDistrictList.clear()
        val service = Retrofit.Builder()
            .baseUrl("https://api.data.gov.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java);

        service.getDistrictWiseData().enqueue(object : Callback<DistrictWise> {

            /* The HTTP call failed. This method is run on the main thread */
            override fun onFailure(call: Call<DistrictWise>, t: Throwable) {
                Log.d("TAG_ D", "An error happened!")
                dismissProgress()
                t.printStackTrace()
            }

            /* The HTTP call was successful, we should still check status code and response body
             * on a production app. This method is run on the main thread */
            override fun onResponse(call: Call<DistrictWise>, response: Response<DistrictWise>) {
                /* This will print the response of the network call to the Logcat */
                dismissProgress()

                Log.d("TAG_ D 1", response.message())

                response.body()?.toString()?.let { Log.d("TAG_ D 2", it) }




//                response.body().records
//
//                myVillageList.addAll()

                response.body()?.toString()?.let { Log.d("TAG_ D 2", it) }


                for(item in response.body()?.records!!)
                {
                    myDistrictList.add(item)
                }


//                mVillageAdapter = MyVillageBaseAdapter(applicationContext, myVillageList)
//                lv?.setAdapter(mVillageAdapter)

                filtertext.setText("")

                lv?.adapter = MyDistrictBaseAdapter(this@MainActivity,myDistrictList);




            }
        })


    }

    fun ShowDatePicker()
    {
        val c = Calendar.getInstance()
        val year1 = c.get(Calendar.YEAR)
        val month1 = c.get(Calendar.MONTH)
        val day1 = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, monthOfYear)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var selectedDate = sdf.format(c.time)

            Log.d("Selected Date : ",selectedDate)



            filtertext.setText(selectedDate)


//            this@MainActivity.runOnUiThread(java.lang.Runnable {
//                MyVillageBaseAdapter(this@MainActivity,myVillageList).filter(selectedDate,"2")// is a Runnable, but does
//            })

            filter(selectedDate,"2")
           // lv!!.invalidate()


           // MyVillageBaseAdapter(this@MainActivity,myVillageList).filter(selectedDate,"2",myVillageList)//

        }, year1, month1, day1)

        dpd.show()


    }







    interface APIService {
        @GET("/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=10&limit=10&filters=Village")
        fun getVillageWiseData(): Call<VillageWise>

        @GET("/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001cdd3946e44ce4aad7209ff7b23ac571b&format=json&offset=10&limit=10&filters=District")
        fun getDistrictWiseData(): Call<DistrictWise>
    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MyVillageBaseAdapter(context: Context, myList: ArrayList<Records>) :
        BaseAdapter() {


        private var myList = ArrayList<Records>()
        private var inflater: LayoutInflater
        private var privatearray: ArrayList<Records>

        private var size = 0

        init {
            this.myList = myList
            inflater = LayoutInflater.from(context)
            privatearray = ArrayList()
            privatearray.addAll(myList)
            size = this.myList.size
        }


        override fun getCount(): Int {
            // TODO Auto-generated method stub
            return myList.size
        }

        override fun getItem(position: Int): Any {
            // TODO Auto-generated method stub
            return myList[position]

        }

        override fun getItemId(position: Int): Long {
            // TODO Auto-generated method stub
            return position.toLong()

        }

        override fun getView(position: Int, view: View?, arg2: ViewGroup): View {
            //LayoutInflater inflater = getLayoutInflater();

            val row: View
            row = inflater.inflate(R.layout.list_item_view, arg2, false)
            val state: TextView
            val district: TextView
            val date: TextView
            val price: TextView
            val market: TextView
            val commodity : TextView

            state = row.findViewById<View>(R.id.statetext) as TextView
            district = row.findViewById<View>(R.id.districttext) as TextView
            date = row.findViewById<View>(R.id.datetext) as TextView
            price = row.findViewById<View>(R.id.pricetext) as TextView
            market = row.findViewById<View>(R.id.markettext) as TextView
            commodity = row.findViewById<View>(R.id.comtext) as TextView

            try
            {
                state.text =  myList[position].state
                district.text =  myList[position].district
                date.text =  myList[position].arrivalDate
                price.text =  myList[position].modalPrice
                market.text =  myList[position].market
                commodity.text =  myList[position].commodity
            }
            catch (w : Exception)
            {


            }



            return row
        }



        // Filter Class



//        fun filter(charTextselected: String,type: String,myListMain: ArrayList<Records>) {
//            Log.d("Filter pass 1 : ",charTextselected)
//            var charText = charTextselected
//
//            charText = charTextselected.toLowerCase(Locale.getDefault())
//
//
//
//
//            this.myList.clear()
//
//            Log.d("Filter pass x 3 : ",myList.size.toString())
//
//            Log.d("Filter pass 4 : ","please print")
//            Log.d("Filter pass 5 : ",privatearray.size.toString())
//
//            if (charText.length == 0) {
//                Log.d("Filter pass 9 : ",privatearray.size.toString())
//                myList.addAll(privatearray)
//            }
//            else
//            {
//                privatearray.forEachIndexed { index, records ->
//
//                    Log.d("Filter pass 6  aaa  : ",records.arrivalDate+"/n"+index)
//
//                    if(type.equals("2"))
//                    {
//                        if (records.arrivalDate.toLowerCase(Locale.getDefault())
//                                .contains(charText))
//                        {
//                            Log.d("Filter pass 8 match found  : ",records.arrivalDate+"/n"+index)
//                            this.myList.add(records)
//
//                        }
//                    }
//                    else
//                    {
//                        if (records.modalPrice.toLowerCase(Locale.getDefault())
//                                .contains(charText))
//                        {
//                            Log.d("Filter pass 8 match found  : ",records.modalPrice+"/n"+index)
//                            this.myList.add(records)
//
//                        }
//                    }
//
//
//
//                }
//            }
//
//
//
//
//
//
//
//            Log.d("Filter pass mylistsize before notify : ",myList.size.toString())
//            this.notifyDataSetChanged()
//
//
//
//
//
//        }

    }

    fun filter(charTextselected: String,type: String) {
        Log.d("Filter pass 1 : ",charTextselected)


       var charText = charTextselected.toLowerCase(Locale.getDefault())




        if(isVillageSelected)
        {
            this.myVillageListFiltered.clear()

            Log.d("Filter pass x 3 : ",myVillageListFiltered.size.toString())

            Log.d("Filter pass 4 : ","please print")
            Log.d("Filter pass 5 : ",myVillageList.size.toString())

            if (charText.length == 0) {
                myVillageListFiltered.addAll(myVillageList)
            }
            else
            {
                myVillageList.forEachIndexed { index, records ->

                    Log.d("Filter pass 6  aaa  : ",records.arrivalDate+"/n"+index)

                    if(type.equals("2"))
                    {
                        if (records.arrivalDate.toLowerCase(Locale.getDefault())
                                .contains(charText))
                        {
                            Log.d("Filter pass 8 match found  : ",records.arrivalDate+"/n"+index)
                            this.myVillageListFiltered.add(records)

                        }
                    }
                    else
                    {
                        if (records.modalPrice.toLowerCase(Locale.getDefault())
                                .contains(charText))
                        {
                            Log.d("Filter pass 8 match found  : ",records.modalPrice+"/n"+index)
                            this.myVillageListFiltered.add(records)

                        }
                    }



                }
            }




            lv?.adapter = MyVillageBaseAdapter(this@MainActivity,myVillageListFiltered);
        }
        else
        {
            this.myDistrictListFiltered.clear()

            Log.d("Filter pass x 3 : ",myDistrictListFiltered.size.toString())

            Log.d("Filter pass 4 : ","please print")
            Log.d("Filter pass 5 : ",myDistrictList.size.toString())

            if (charText.length == 0) {
                myDistrictListFiltered.addAll(myDistrictList)
            }
            else
            {
                myDistrictList.forEachIndexed { index, records ->

                    Log.d("Filter pass 6  aaa  : ",records.arrivalDate+"/n"+index)

                    if(type.equals("2"))
                    {
                        if (records.arrivalDate.toLowerCase(Locale.getDefault())
                                .contains(charText))
                        {
                            Log.d("Filter pass 8 match found  : ",records.arrivalDate+"/n"+index)
                            this.myDistrictListFiltered.add(records)

                        }
                    }
                    else
                    {
                        if (records.modalPrice.toLowerCase(Locale.getDefault())
                                .contains(charText))
                        {
                            Log.d("Filter pass 8 match found  : ",records.modalPrice+"/n"+index)
                            this.myDistrictListFiltered.add(records)

                        }
                    }



                }
            }

            lv?.adapter = MyDistrictBaseAdapter(this@MainActivity,myDistrictListFiltered);
        }








        //this.notifyDataSetChanged()





    }


    class MyDistrictBaseAdapter(internal var context: Context, myList: ArrayList<DRecords>) :
        BaseAdapter() {


        internal var myList = ArrayList<DRecords>()
        internal var inflater: LayoutInflater
        private val privatearray: ArrayList<DRecords>

        init {
            this.myList = myList
            inflater = LayoutInflater.from(this.context)
            privatearray = ArrayList()
            privatearray.addAll(myList)
        }


        override fun getCount(): Int {
            // TODO Auto-generated method stub
            return myList.size
        }

        override fun getItem(position: Int): Any {
            // TODO Auto-generated method stub
            return myList[position]
        }

        override fun getItemId(position: Int): Long {
            // TODO Auto-generated method stub
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, arg2: ViewGroup): View {
            //LayoutInflater inflater = getLayoutInflater();

            val row: View
            row = inflater.inflate(R.layout.list_item_view, arg2, false)
            val state: TextView
            val district: TextView
            val date: TextView
            val price: TextView
            val market: TextView
            val commodity : TextView

            state = row.findViewById<View>(R.id.statetext) as TextView
            district = row.findViewById<View>(R.id.districttext) as TextView
            date = row.findViewById<View>(R.id.datetext) as TextView
            price = row.findViewById<View>(R.id.pricetext) as TextView
            market = row.findViewById<View>(R.id.markettext) as TextView
            commodity = row.findViewById<View>(R.id.comtext) as TextView

            state.text =  myList[position].state
            district.text =   myList[position].district
            date.text =  myList[position].arrivalDate
            price.text =  myList[position].modalPrice
            market.text =  myList[position].market
            commodity.text =  myList[position].commodity


            return row
        }

    }


    fun showProgress()
    {
        try
        {


            spinner!!.setVisibility(View.VISIBLE);

        }
        catch (e : Exception)
        {

        }

    }

    fun dismissProgress()
    {
        try
        {
            spinner!!.setVisibility(View.GONE);
        }
        catch (e : Exception)
        {

        }

    }




}

