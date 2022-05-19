package com.example.login2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Para conectarse a la api necesitas una direccion de red en internet no admite local
    //String urlApi = "http://192.16.16.40:8080/login";
    String urlApi = "http://192.168.1.227:8080/login";
    //Email Field
    private EditText etEmail;
    //Password Field
    private EditText etPassword;
    //Button Event SignIn
    private Button btnSign;
    //Bar
    private ProgressBar loadingPB;
    //Response TextView
    private TextView responseTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Email Field
        etEmail = (EditText) this.findViewById(R.id.email);
        //Password Field
        etPassword = (EditText) this.findViewById(R.id.password);
        //Button Validate
        btnSign = (Button) this.findViewById(R.id.button);
        //Bar
        loadingPB = (ProgressBar) this.findViewById(R.id.idLoadingPB);
        //Response TextView
        responseTV = (TextView) this.findViewById(R.id.idTVResponse) ;


        //Event Click Listener to our button
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEmail.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()){
                    //Toast nos permite mostrar mensaje en pantalla -- como tipo alert
                    Toast.makeText(MainActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                // calling a method to post the data and passing our name and job.
                postDataUsingVolley(etEmail.getText().toString(), etPassword.getText().toString(),urlApi);

            }
        });

    }

        //Method to send data with volley
        private void postDataUsingVolley(String email,String pass,String pathApi) {

            //Show Bar loading
            loadingPB.setVisibility(View.VISIBLE);

            // creating a new variable for our request queue
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            // on below line we are calling a string
            // request method to post the data to our API
            // in this we are calling a post method.
            StringRequest request = new StringRequest(Request.Method.POST, pathApi, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // inside on response method we are
                    // hiding our progress bar
                    // and setting data to edit text as empty
                    loadingPB.setVisibility(View.GONE);
                    //Set Empty the fields
                    etEmail.setText("");
                    etPassword.setText("");

                    // on below line we are displaying a success toast message.
                    Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
                    try {
                        // on below line we are parsing the response
                        // to json object to extract data from it.
                        JSONObject respObj = new JSONObject(response);

                        // below are the strings which we
                        // extract from our json object.
                        String email = respObj.getString("nombre");
                        String password = respObj.getString("email");

                        Log.d("Usuario Recibido", "Email "+email+" Pass _ "+password);

                        // on below line we are setting this string s to our text view.
                        responseTV.setText("Email : " + email + "\n" + "Password : " + pass);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("Error Response","Error == > "+error.toString());
                    // method to handle errors.
                    Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // below line we are creating a map for
                    // storing our values in key and value pair.
                    Map<String, String> params = new HashMap<String, String>();

                    // on below line we are passing our key
                    // and value pair to our parameters.
                    //Aqui estrictamente se pone los valores que recibira el json
                    params.put("email", email);
                    params.put("password", pass);

                    // at last we are
                    // returning our params.
                    return params;
                }
            };

                // below line is to make
                // a json object request.
		        queue.add(request);
            }

}