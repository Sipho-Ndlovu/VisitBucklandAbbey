package com.example.visitbucklandabbeyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttractionsPage extends AppCompatActivity implements AttractionsRecyclerViewInterface {
    //RecyclerView variables
    RecyclerView recyclerView;
    List<Attraction> Attractions;
    attractionsAdapter adapter;
    //OnClick variables
    private TextView Name;
    private TextView Facilities;
    private TextView OpeningTime;
    private TextView TicketPrices;
    //Header variables
    private TextView btnHome;
    private TextView btnAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions_page);
        btnHome = (TextView) findViewById(R.id.btnHome);
        btnAdmin = (TextView) findViewById(R.id.btnAdmin);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenHome();}
        });
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {OpenAdmin();}
        });

        recyclerView = findViewById(R.id.AttractionsList3);
        Attractions = new ArrayList<>();
        extractAttractions();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new attractionsAdapter(this, Attractions, this);
        recyclerView.setAdapter(adapter);
    }

    private void OpenAdmin() {
        Intent intent = new Intent(AttractionsPage.this, AdminPage.class);
        startActivity(intent);
    }

    private void OpenHome() {
        Intent intent = new Intent(AttractionsPage.this, HomePage.class);
        startActivity(intent);
    }

    private void extractAttractions() {
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

    @Override
    public void OnItemClick(int position) {
        Name = (TextView) findViewById(R.id.name);
        Facilities = (TextView) findViewById(R.id.Facilities);
        OpeningTime = (TextView) findViewById(R.id.OpeningTime);
        TicketPrices = (TextView) findViewById(R.id.TicketPrices);

        Name.setText(Attractions.get(position).getName());
        Facilities.setText("The facilities available at this attraction are: " + Attractions.get(position).getFacilities());
        OpeningTime.setText("Opening times : " + Attractions.get(position).getOpeningTime());
        TicketPrices.setText("Ticket Prices: " + Attractions.get(position).getTicketPrices());
    }
}