package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    String cedula;
    TextView textViewCedula;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       cedula = getIntent().getExtras().getString("cedula");
       textViewCedula = findViewById(R.id.textViewCedula_);
       textViewCedula.setText(cedula);
    }
    public void IrClientes(View view){
        Intent intent= new Intent(this, ClienteMain.class);
        startActivity(intent);
    }

    public void IrPro(View view){
        Intent intent= new Intent(this,Producto1Main.class);
        startActivity(intent);
    }

    public void IrVentas(View view){
        try{

            Toast.makeText(getApplicationContext(),"REALIZAR COMPRA",Toast.LENGTH_SHORT).show();

        }catch(Exception e){

        }

    }
    public void IrCompras(View view){
        Intent intent= new Intent(this, ComprasMain.class);
        intent.putExtra("cedula",cedula);
        startActivity(intent);
    }



}