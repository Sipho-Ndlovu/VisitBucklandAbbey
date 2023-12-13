package com.example.visitbucklandabbeyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button btnReg;
    private Button btnLogIn;
    private EditText Sid;
    private EditText Sname;
    boolean logged = false;
    int checks = 0;
    Student LoggedIn = new Student();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg = (Button) findViewById(R.id.btnReg);
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenReg();
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttemptLogIn();
            }
        });
    }

    public void OpenReg() {
        Intent intent = new Intent(this, RegistrationPage.class);
        startActivity(intent);
    }

    public void AttemptLogIn() {
        checks = 0;
        logged = false;
        Sname = (EditText) findViewById(R.id.LastName);
        Sid = (EditText) findViewById(R.id.StudentID);
        String sID = Sid.getText().toString();
        String SecondName = Sname.getText().toString();

        if (SecondName.equalsIgnoreCase("")) {
            Sname.setError("Enter valid Last name; Input is empty!");
        }
        else {
            checks ++;
        }
        if (sID.equalsIgnoreCase("")){
            Sid.setError("Enter valid StudentID; Input is empty!");
        }
        else {
            checks++;
        }
        if (checks == 2) {
            RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
            JsonArrayRequest JsonArray = new JsonArrayRequest(Request.Method.GET, "http://web.socem.plymouth.ac.uk/COMP2000/api/students/", null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject studentObject = null;
                                try {
                                    int ID = Integer.parseInt(sID);
                                    studentObject = response.getJSONObject(i);

                                    Student student = new Student();
                                    student.setStudentID(studentObject.getInt("studentID"));
                                    student.setFirst_Name(studentObject.getString("first_Name").toString());
                                    student.setSecond_Name(studentObject.getString("second_Name").toString());
                                    if (student.getSecond_Name().equals(SecondName)) {
                                        if (student.getStudentID() == ID) {
                                            LoggedIn.setStudentID(student.getStudentID());
                                            LoggedIn.setFirst_Name(student.getFirst_Name());
                                            LoggedIn.setSecond_Name(student.getSecond_Name());
                                            logged = true;
                                        }
                                    }
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (logged == true) {
                                OpenLogIn();
                            }
                            else {
                                Sname.setError("Log In Error; The Last Name and StudentID pair don't match any existing accounts!");
                                Sid.setError("Log In Error; The Last Name and StudentID pair don't match any existing accounts!");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("tag", "onErrorResponse1: " + error.getMessage());
                }
            });
            queue.add(JsonArray);
        }
    }

    public void OpenLogIn() {
        Toast.makeText(getBaseContext(), "Welcome " + LoggedIn.getFirst_Name() + " " + LoggedIn.getSecond_Name() + "!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}