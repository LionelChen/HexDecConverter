package com.example.zp.hexdecconverter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoredActivity extends AppCompatActivity {

    private DatabaseReference myDatabase;
    private String[] listValues;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listValues = new String[] { "item1", "item2", "item3"};

        list = new ArrayList<String>();
        for (int i = 0; i < listValues.length; ++i) {
            list.add(listValues[i]);
        }


        //Get Firebase instance
        myDatabase = FirebaseDatabase.getInstance().getReference("message");

        //Add Firebase event listener (Update data only one time)
        myDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView text = findViewById(R.id.jsonField);
                text.setText(dataSnapshot.getValue().toString());
                try {
                    JSONObject obj = new JSONObject(dataSnapshot.getValue().toString());


                    Iterator<String> keys = obj.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        if (obj.get(key) instanceof JSONObject) {
                            Log.w("Each", ((JSONObject) ((JSONObject) obj.get(key))).toString());
                            // 加上就崩?
                            //list.add((obj.get(key)).toString());
                        }
                    }



                } catch (JSONException e) {
                    //some exception handler code.
                    Log.w("JSON Exception", e.toString());
                }

                Log.w("onDataChange", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ListView listview = (ListView) findViewById(R.id.listView);
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);


        //Add Firebase event listener (Update data on remote database change)
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                TextView text = findViewById(R.id.jsonField);

                try {
                    JSONObject obj = new JSONObject(dataSnapshot.getValue().toString());

                    text.setText(obj.toString());
                    Iterator<String> keys = obj.keys();

                    while(keys.hasNext()) {
                        String key = keys.next();
                        if (obj.get(key) instanceof JSONObject) {
                            Log.w("Each", ((JSONObject) obj.get(key)).toString());
                            // TODO: list.add加上就崩?
                            // list.add(((JSONObject) obj.get(key)).toString());
                            // list.add("hi");
                            adapter.notifyDataSetChanged();
                        }
                    }



                } catch (JSONException e) {
                    //some exception handler code.
                    Log.w("JSON Exception", e.toString());
                }

                Log.w("onDataChange", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        myDatabase.addValueEventListener(postListener);


        // Listview item on click delete
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                //Animate time
                view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }


    //Array Adapter
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }


        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
