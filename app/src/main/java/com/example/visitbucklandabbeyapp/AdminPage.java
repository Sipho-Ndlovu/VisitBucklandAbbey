package com.example.visitbucklandabbeyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminPage extends AppCompatActivity implements AdminRecyclerViewInterface {
    //Initial Popup variables
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog Dialog;
    private TextView Title;
    private EditText Pin;
    private Button btnPin;
    private Button btnCancel;
    int PIN = 1232;
    //RecyclerView variables
    RecyclerView recyclerView;
    List<Attraction> Attractions;
    adminAdapter adapter;
    //Header variables
    private TextView btnHome;
    private TextView btnAttractions;
    // Page variables
    private Button btnCreate;
    //Create Attraction Popup variables
    private int Checks = 0;
    private boolean exists = false;
    private boolean created = false;
    private EditText Name;
    private EditText Facilities;
    private EditText OpeningTime;
    private EditText TicketPrices;
    private EditText HistoricalInfo;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnHome = (TextView) findViewById(R.id.btnHome);
        btnAttractions = (TextView) findViewById(R.id.btnAttractions);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenHome();}
        });
        btnAttractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenAttractions();}
        });
        btnCreate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CreateNewAttractionDialog();
                return true;
            }
        });


        recyclerView = findViewById(R.id.AttractionsList);
        Attractions = new ArrayList<>();
        extractAttractions();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new adminAdapter(this, Attractions, this);
        recyclerView.setAdapter(adapter);

        CreatePageDialog();
    }

    private void OpenAttractions() {
        Intent intent = new Intent(AdminPage.this, AttractionsPage.class);
        startActivity(intent);
    }

    private void CreateNewAttractionDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View createPopup = getLayoutInflater().inflate(R.layout.admin_create_popup_page, null);
        Title = (TextView) createPopup.findViewById(R.id.Title);
        Name = (EditText) createPopup.findViewById(R.id.Name);
        Facilities = (EditText) createPopup.findViewById(R.id.Facilities);
        OpeningTime = (EditText) createPopup.findViewById(R.id.OpeningTime);
        TicketPrices = (EditText) createPopup.findViewById(R.id.TicketPrices);
        HistoricalInfo = (EditText) createPopup.findViewById(R.id.HistoricalInfo);
        btnAdd = (Button) createPopup.findViewById(R.id.btnAdd);
        btnCancel = (Button) createPopup.findViewById(R.id.btnCancel);

        dialogBuilder.setView(createPopup);
        Dialog = dialogBuilder.create();
        Dialog.show();
        Dialog.getWindow().setLayout(1000, 1900);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checks = 0;
                String name = Name.getText().toString();
                String facilities = Facilities.getText().toString();
                String openingTime = OpeningTime.getText().toString();
                String ticketPrices = TicketPrices.getText().toString();
                Attraction attraction = new Attraction();
                attraction.setId(0);
                attraction.setName(name);
                attraction.setFacilities(facilities);
                attraction.setOpeningTime(openingTime);
                attraction.setTicketPrices(ticketPrices);
                if (name.equalsIgnoreCase("")) {
                    Name.setError("Enter valid Name; Input is empty!");
                }
                else {
                    Checks++;
                }
                if (facilities.equalsIgnoreCase("")) {
                    Facilities.setError("Enter valid Facilities; Input is empty!");
                }
                else {
                    Checks++;
                }
                if (openingTime.equalsIgnoreCase("")) {
                    OpeningTime.setError("Enter valid Opening Time; Input is empty!");
                }
                else {
                    Checks++;
                }
                if (ticketPrices.equalsIgnoreCase("")) {
                    TicketPrices.setError("Enter valid Ticket prices; Input is empty!");
                }
                else {
                    Checks++;
                }
                if (Checks == 4) {
                    createAttractions(attraction);
                }
            }
        });
    }

    public void CreatePageDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View adminPopup = getLayoutInflater().inflate(R.layout.admin_popup_page, null);
        Title = (TextView) adminPopup.findViewById(R.id.Title);
        Pin = (EditText) adminPopup.findViewById(R.id.Pin);
        btnPin = (Button) adminPopup.findViewById(R.id.btnPin);
        btnCancel = (Button) adminPopup.findViewById(R.id.btnCancel);

        dialogBuilder.setView(adminPopup);
        Dialog = dialogBuilder.create();
        Dialog.show();
        Dialog.getWindow().setLayout(1000, 1000);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPage.this, HomePage.class);
                startActivity(intent);
            }
        });
        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pin = (EditText) adminPopup.findViewById(R.id.Pin);
                String ePin = Pin.getText().toString();

                if (ePin.equalsIgnoreCase("")){
                    Pin.setError("Enter valid PIN; Input is empty!");
                }
                else{
                    int pin = Integer.parseInt(ePin);
                    if (pin == PIN) {
                        Dialog.dismiss();
                    }
                    else {
                        Pin.setError("Enter valid PIN; PIN is incorrect!");
                    }
                }
            }
        });
    }

    public void extractAttractions() {
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/attractions/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getBaseContext(), "Volley Running", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject attractionObject = response.getJSONObject(i);

                        Attraction attraction = new Attraction();
                        attraction.setId(attractionObject.getInt("id"));
                        attraction.setName(attractionObject.getString("name"));
                        attraction.setFacilities(attractionObject.getString("facilities"));
                        attraction.setOpeningTime(attractionObject.getString("openingTime"));
                        attraction.setTicketPrices(attractionObject.getString("ticketPrices"));
                        Attractions.add(attraction);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArray);
    }

    public void OpenHome(){
        Intent intent = new Intent(AdminPage.this, HomePage.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteLongClick(int position, int id) {
        Attractions.remove(position);
        adapter.notifyDataSetChanged();

        String ID = String.valueOf(id);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", id);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.DELETE, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/attractions/" + ID, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getBaseContext(), "Attraction Deleted!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonObject);
    }
    public void createAttractions(Attraction attraction) {
        exists = false;
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/attractions/", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int size = 0;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject attractionObject = null;
                    try {
                        attractionObject = response.getJSONObject(i);

                        Attraction attraction1 = new Attraction();
                        size = attractionObject.getInt("id");
                        attraction1.setName(attractionObject.getString("name"));
                        attraction1.setFacilities(attractionObject.getString("facilities"));
                        attraction1.setOpeningTime(attractionObject.getString("openingTime"));
                        attraction1.setTicketPrices(attractionObject.getString("ticketPrices"));
                        if (attraction.getName().equals(attraction1.getName())) {
                            exists = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (exists == false) {
                    size++;
                    attraction.setId(size);
                    Map<String, Object> newAttraction = new ArrayMap<>();
                    newAttraction.put("id", size);
                    newAttraction.put("name", attraction.getName());
                    newAttraction.put("facilities", attraction.getFacilities());
                    newAttraction.put("openingTime", attraction.getOpeningTime());
                    newAttraction.put("ticketPrices", attraction.getTicketPrices());

                    JSONObject request = new JSONObject(newAttraction);
                    JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/attractions/", request,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("tag", "onErrorResponse1 " + error.getMessage());
                        }
                    });
                    queue.add(jsonObject);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(jsonArray);
        if (created == false) {
            Dialog.dismiss();
            Attractions.add(attraction);
            adapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(), "Attraction Created!" , Toast.LENGTH_SHORT).show();
        }
    }
}