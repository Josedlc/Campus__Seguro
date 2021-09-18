package mx.itson.campus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Item> listItems;

    TextView txtHora, txtDia, txtLugar;
    ImageView btnCerrar;
    Button btnRegistrar;
    public static Socket socket;
    private Boolean servidor;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView ivFoto;


        MyViewHolder( View itemView)  {
            super(itemView);
            ivFoto= (CircleImageView) itemView.findViewById(R.id.ivFoto);
            txtHora= (TextView) itemView.findViewById(R.id.txtHora);
            txtDia= (TextView) itemView.findViewById(R.id.txtDia);
            txtLugar=  (TextView) itemView.findViewById(R.id.txtLugar);
            btnCerrar = (ImageView) itemView.findViewById(R.id.btnCerrar);
            btnRegistrar = (Button) itemView.findViewById(R.id.btnRegistrar);

        }

        @Override
        public void onClick(View view) {



            switch (view.getId()){
                case R.id.btnCerrar:
                   InitActivity.ListItems.remove(getAdapterPosition());

                    notifyItemRemoved(getAdapterPosition());

                    if(listItems.size() == 0){
                        InitActivity.viewSwitcher.showPrevious();
                    }

                    break;
                case R.id.btnRegistrar:
                    showPopUp(listItems.get(getAdapterPosition()).getFoto(),listItems.get(getAdapterPosition()).getId());
                    break;
            }
        }


        void setOnClickListeners(){
            btnCerrar.setOnClickListener(this);
            btnRegistrar.setOnClickListener(this);

        }
    }


    public RecyclerViewAdapter(Context context, ArrayList<Item> listItems){
        this.context=context;
        this.listItems=listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_lista,parent,false);
        return new MyViewHolder(contentView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = listItems.get(position);
      //  final String pureBase64Encoded = item.getFoto();//.substring(item.getFoto().indexOf(",")  + 1);
        //byte[] decodedString = Base64.decode(pureBase64Encoded, 0);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        final String encodedString = "data:image/jpg;base64,"+item.getFoto();
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",") + 1);
        final byte[] decodedBytes = Base64.decode(item.getFoto(), Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);


        /*final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);*/

        //Holder holder1 =(Holder)holder;
        holder.ivFoto.setImageBitmap(decodedBitmap);
       //Picasso.get().load(decodedBitmap).into(Holder.ivFoto));
       // Holder.ivFoto.setImageResource(item.getFoto());
        //Picasso.
        //holder1.ivFoto.setImageBitmap(decodedBitmap);
        txtHora.setText(item.getHora());
        txtDia.setText(item.getDia());
        txtLugar.setText(item.getLugar());
        holder.setOnClickListeners();
    }



    @Override
    public int getItemCount() {
        return listItems.size();
    }

  // public String fromBase64ToBitmap(String string){
//byte[] decodestring = Base64.decode(string, Base64.DEFAULT);
 //Bitmap bitmap = BitmapFactory.decodeByteArray(decodestring, 0, decodestring.length);
 //return BitmapDrawable;

   // }

    public void showPopUp(String foto, final int id){

        final PopupWindow popupWindow;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popUpView = inflater.inflate(R.layout.registrar,null);


        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focuseable = true;

        popupWindow = new PopupWindow(popUpView,width,height,focuseable);

        ImageView cerrar = popUpView.findViewById(R.id.fondoRegistrar);
        ImageView imagen = popUpView.findViewById(R.id.ivwPerfil);
        ImageView btnFoto = popUpView.findViewById(R.id.imageView2);
        Button btnAceptar = popUpView.findViewById(R.id.btnAceptar);
        final EditText txtNombre = popUpView.findViewById(R.id.txtNombre);
        final EditText txtArea = popUpView.findViewById(R.id.txtArea);
        final EditText txtId = popUpView.findViewById(R.id.txtID);
        final String encodedString = "data:image/jpg;base64,"+foto;
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",") + 1);
        final byte[] decodedBytes = Base64.decode(foto, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        imagen.setImageBitmap(decodedBitmap);

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

btnAceptar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        JSONObject data = new JSONObject();
        try{
            data.put("id", id);
            data.put("nombre", txtNombre.getText());
            data.put("imagen", pureBase64Encoded);
            data.put("identificacion", txtId.getText());
            data.put("lugar", txtArea.getText());
        }catch(Exception e){

        }

        InitActivity.socket.emit("agregarRostro", String.valueOf(data));
        popupWindow.dismiss();
    }
});

btnFoto.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});


        popupWindow.setAnimationStyle(R.style.Animation);

        popupWindow.showAtLocation(new LinearLayout(context), Gravity.BOTTOM,0,0);
    }


}
