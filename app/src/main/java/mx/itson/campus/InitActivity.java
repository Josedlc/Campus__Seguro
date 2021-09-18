package mx.itson.campus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class InitActivity extends AppCompatActivity {
    private RecyclerViewAdapter rvAdapter;
    public static ViewSwitcher viewSwitcher;
    public static ArrayList<Item> ListItems = new ArrayList<>();
    public static Socket socket;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private Boolean servidor;
    private Dialog epicDialog;
    private TextView message;
    private RelativeLayout relativeLayout;
    private PopupWindow popupWindow;
    private boolean conexion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        viewSwitcher = findViewById(R.id.viewSwitcher);
        recyclerView = findViewById(R.id.recyclerView);


       // recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewAdapter(this, ListItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());





        try {
            socket = IO.socket("http://10.232.14.179:4404");
            socket.connect();
            socket.emit("comprobarEstado", 0);
            socket.on("connect_error", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if(!conexion){
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();

                                }
                                ShowNegativePopup2();
                            }

                            conexion = true;


                        }


                    });

                }
            });

            socket.on("connect", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            conexion = false;

                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();

                            }



                        }


                    });

                }
            });


            socket.on("reconnect", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            socket.emit("comprobarEstado", 0);




                        }


                    });

                }
            });





            socket.on("respuestaEstado", new Emitter.Listener() {
                @Override
                public void call(final Object... args) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject data = (JSONObject) args[0];
                            try {
                                //extract data from fired event


                                Boolean state = data.getBoolean("estado");


                                if(state){
                                 servidor=true;
                                    if (popupWindow != null && popupWindow.isShowing()) {
                                        popupWindow.dismiss();

                                    }

                                }else{
                                 servidor=false;
                                    if (!popupWindow.isShowing()) {
                                        ShowNegativePopup();

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("Socket error", e.toString());
                            }


                        }


                    });

                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();


        }

        socket.on("alerta", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                          //  JSONObject data2 = (JSONObject) data.getJSONObject("imagen");
                            String nickname = data.getString("imagen");
                            int id = data.getInt("id");
                            String dia = data.getString("dia");
                            String hora = data.getString("hora");
                            String lugar = data.getString("lugar");
                            nickname = nickname.replace("\\/","/");

                            if(ListItems.size() == 0){
                                viewSwitcher.showNext();
                            }
                            ListItems.add(new Item(nickname, dia, hora, lugar, id));
                            adapter.notifyItemInserted(ListItems.size());

                            Log.e("Mensaje",  data.getString("imagen"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Socket error", e.toString());
                        }


                    }


                });

            }
        });

        socket.on("estado", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event


                            Boolean state = data.getBoolean("estado");


                            if(state){

                                    popupWindow.dismiss();


                                ShowPositivePopup();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        popupWindow.dismiss();
                                        handler.removeCallbacksAndMessages(null);
                                    }
                                }, 3000);

                            }else{
                                if (popupWindow != null && popupWindow.isShowing()) {
                                    popupWindow.dismiss();

                                }
                                if (!popupWindow.isShowing()) {
                                    ShowNegativePopup();

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Socket error", e.toString());
                        }


                    }


                });

            }
        });


        socket.on("registroVisita", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event


                            int id = data.getInt("id");
                            for(int i = 0; i <= ListItems.size(); i++){
                                if(id == ListItems.get(i).getId()){
                                    ListItems.remove(i);
                                    adapter.notifyItemRemoved(i);
                                    break;
                                }

                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Socket error", e.toString());
                        }


                    }


                });

            }
        });


    }




    public void ShowPositivePopup(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.online,null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focuseable = false;

         popupWindow = new PopupWindow(popUpView,width,height,focuseable);

        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.showAtLocation(new LinearLayout(InitActivity.this), Gravity.BOTTOM,0,0);
        //popupWindow.showAsDropDown(relativeLayout);
        //popupWindow = new PopupWindow();


        //epicDialog = new Dialog(InitActivity.this);

        //epicDialog.setContentView(R.layout.online);

        //epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //epicDialog.show();
    }

    public void ShowNegativePopup(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.offline,null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focuseable = false;

       popupWindow = new PopupWindow(popUpView,width,height,focuseable);

        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.showAtLocation(new LinearLayout(InitActivity.this), Gravity.BOTTOM,0,0);
    }

    public void ShowNegativePopup2(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.offline_2,null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focuseable = false;

        popupWindow = new PopupWindow(popUpView,width,height,focuseable);

        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.showAtLocation(new LinearLayout(InitActivity.this), Gravity.BOTTOM,0,0);
    }



}