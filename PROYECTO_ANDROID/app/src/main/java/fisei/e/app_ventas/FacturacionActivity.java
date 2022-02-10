package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FacturacionActivity extends AppCompatActivity {
    ArrayList<String> lstProductos = new ArrayList<>();
    ListView listviewProducto ;
    String numerof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facturacion);

        try {
            listviewProducto = findViewById(R.id.listviewProducto);

            lstProductos = getIntent().getExtras().getStringArrayList("lstProductos");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1,lstProductos );
            listviewProducto.setAdapter(adapter);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        numerof= getIntent().getExtras().getString("numerof");
        Toast.makeText(getApplicationContext(),numerof,Toast.LENGTH_SHORT).show();


    }
    public void FinalizarFact(View view){

        Intent intent= new Intent(this, VentaMain.class);
        intent.putExtra("numerof",numerof);
        startActivity(intent);

    }




}
