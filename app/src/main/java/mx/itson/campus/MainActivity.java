package mx.itson.campus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText pass;
    EditText user;
    boolean estado;

    private Volleys volleyS;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyS = Volleys.getInstance(this);
        requestQueue = volleyS.getRequestQueue();

        btn= (Button) findViewById(R.id.sesion);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InitActivity.class));



            }
        });

       /* pass=(EditText) findViewById(R.id.txtpass);
        user=(EditText) findViewById(R.id.txtuser);
        final EditText[] misCampos = {user, pass};

        btn= (Button) findViewById(R.id.sesion);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampoVacio(misCampos))
                    Toast.makeText(MainActivity.this,"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
                else
                    iniciarSesion(user.getText().toString(), pass.getText().toString());

            }
        });*/




    }

  /*  public boolean validarCampoVacio(EditText[] campos){

        for(int i=0; i<campos.length; i++){
            String cadena = campos[i].getText().toString();
            if(cadena.trim().isEmpty()){
                return true;
            }

        }
        return false;
    }


    public void iniciarSesion(final String usuario, final String contrasena){

        final ProgressDialog loading = ProgressDialog.show(this,"Iniciando sesion","Espere por favor",false,true);

        String url = "http://192.168.0.88:3000/login";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {

                            JSONObject obj = new JSONObject(response);


                            if (Boolean.valueOf(obj.getString("ok")) == false){
                                loading.dismiss();
                                Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectas", Toast.LENGTH_SHORT).show();
                            }else {


                                //                 Log.d("My App", obj.getJSONObject("usuario").getString("nombre"));
                                //Log.d("My App", obj.getString("email"));
                                loading.dismiss();
                                startActivity(new Intent(MainActivity.this, InitActivity.class));
                                finish();
                            }
                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + t + "\"");
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // As of f605da3 the following should work
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                if(!obj.getBoolean("ok")){
                                    Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectas", Toast.LENGTH_LONG);
                                    loading.dismiss();
                                }
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                e2.printStackTrace();
                            }
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", usuario);
                params.put("password", contrasena);

                return params;
            }


        };



        requestQueue.add(postRequest);


    }
*/
}
