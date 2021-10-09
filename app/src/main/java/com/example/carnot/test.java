//package com.example.myapplication;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.example.myapplication.ResponsePojoClass.Records;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//public class MyVillageBaseAdapter extends BaseAdapter {
//    ArrayList<Records> myList = new ArrayList<Records>();
//    LayoutInflater inflater;
//    Context context;
//    private ArrayList<Records> privatearray;
//    public MyVillageBaseAdapter(Context context,ArrayList<Records> myList) {
//        this.myList = myList;
//        this.context = context;
//        inflater = LayoutInflater.from(this.context);
//        privatearray=new ArrayList<Records>();
//        privatearray.addAll(myList);
//    }
//
//
//    @Override
//    public int getCount() {
//        // TODO Auto-generated method stub
//        return myList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        // TODO Auto-generated method stub
//        return myList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        // TODO Auto-generated method stub
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup arg2) {
//        //LayoutInflater inflater = getLayoutInflater();
//
//        View row;
//        row = inflater.inflate(R.layout.list_item_view, arg2, false);
//        TextView state, district,date,price;
//
//        state=(TextView) row.findViewById(R.id.statetext);
//        district=(TextView) row.findViewById(R.id.districttext);
//        date=(TextView) row.findViewById(R.id.datetext);
//        price=(TextView) row.findViewById(R.id.pricetext);
//
//
//        return (row);
//    }
//    // Filter Class
//    public void filter(String charText) {
//
//        charText = charText.toLowerCase(Locale.getDefault());
//        myList.clear();
//        if(charText.length()==0){
//            myList.addAll(privatearray);
//        }
//        else{
//            for (Records c : privatearray) {
//                if (c.getModalPrice().toLowerCase(Locale.getDefault())
//                        .contains(charText)) {
//                    myList.add(c);
//                }
//            }
//        }
//        notifyDataSetChanged();
//
//
//    }}