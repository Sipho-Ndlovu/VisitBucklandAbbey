package com.example.visitbucklandabbeyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationPage extends AppCompatActivity {
    private Button btnReg;
    private EditText Sid;
    private EditText Fname;
    private EditText Lname;
    boolean created = false;
    boolean exists = false;
    int checks = 0;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        btnReg = (Button) findViewById(R.id.btnReg);
        Toast.makeText(getBaseContext(), "Toast working!", Toast.LENGTH_SHORT).show();
    }

    public void RunReg(View view) {
        checks = 0;
        exists = false;
        Sid = (EditText) findViewById(R.id.StudentID);
        Fname = (EditText) findViewById(R.id.FirstName);
        Lname = (EditText) findViewById(R.id.LastName);
        String sID = Sid.getText().toString();
        String FirstName = Fname.getText().toString();
        String LastName = Lname.getText().toString();

        if (sID.equalsIgnoreCase("")){
            Sid.setError("Enter valid StudentID; Input is empty!");
        }
        else {
            checks ++;
        }
        if (FirstName.equalsIgnoreCase("")) {
            Fname.setError("Enter valid First name; Input is empty!");
        }
        else {
            checks ++;
        }
        if (LastName.equalsIgnoreCase("")){
            Lname.setError("Enter valid Last name; Input is empty!");
        }
        else {
            checks++;
        }
        if (checks == 3) {
            int ID = Integer.parseInt(sID);

            RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
            JsonArrayRequest jsonArray = new JsonArrayRequest(Request.Method.GET, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/students/", null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject studentObject = null;
                        try {
                            studentObject = response.getJSONObject(i);

                            Student student = new Student();
                            student.setStudentID(studentObject.getInt("studentID"));
                            student.setFirst_Name(studentObject.getString("first_Name").toString());
                            student.setSecond_Name(studentObject.getString("second_Name").toString());
                            if (student.getStudentID() == ID) {
                                exists = true;
                            }
                            if (exists == false) {
                                Map<String, Object> Accounts = new ArrayMap<>();
                                Accounts.put("studentID", ID);
                                Accounts.put("first_Name", FirstName);
                                Accounts.put("second_Name", LastName);

                                JSONObject request = new JSONObject(Accounts);
                                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, "http://web.socem.plymouth.ac.uk/COMP2000/api/referral/students/", request,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                created = true;
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("tag", "onErrorResponse1: " + error.getMessage());
                                    }
                                });
                                queue.add(jsonObject);
                            }
                            else{
                                Sid.setError("Enter valid StudentID; User Already exists!");
                            }
                        }
                        catch (JSONException e) {
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
            if (created == false){
                Toast.makeText(getBaseContext(), "Account Created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}