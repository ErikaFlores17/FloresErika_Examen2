package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Producto1Main extends AppCompatActivity {
    EditText editTextBusq;
    CheckBox checkbox_Codigo;
    CheckBox checkbox_Nombre;

    TextView textViewCodigoP;
    TextView textViewNombreP;
    TextView textViewPUnitario;
    TextView textViewCostoP;
    TextView tvstock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto1_main);

        editTextBusq=(EditText) findViewById(R.id.editTextBusq);
        checkbox_Codigo=(CheckBox) findViewById(R.id.checkbox_Codigo);
        checkbox_Nombre=(CheckBox) findViewById(R.id.checkbox_Nombre);

        textViewCodigoP=(TextView) findViewById(R.id.textViewCodigoP);
        textViewNombreP=(TextView) findViewById(R.id.textViewNombreP);
        textViewPUnitario=(TextView) findViewById(R.id.textViewPUnitario);
        textViewCostoP=(TextView) findViewById(R.id.textViewCostoP);
        tvstock=(TextView) findViewById(R.id.tvstock);
    }
    public Connection conexionDB(){
        Connection conex=null;
        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);


            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conex= DriverManager.getConnection("jdbc:jtds:sqlserver://FacturaVentas.mssql.somee.com;" +
                    "databaseName=FacturaVentas;user=Dcaizaguano_SQLLogin_1;password=tdgxfzu7g6");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conex;
    }
    public void consultar_Prod(String cadena){
        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select * from Productos where "+cadena+" like'%"+editTextBusq.getText()+"%'");
            if(rs.next()){
                textViewCodigoP.setText(rs.getString(1));
                textViewNombreP.setText(rs.getString(2));
               textViewPUnitario.setText(rs.getString(3));
                textViewCostoP.setText(rs.getString(4));
               tvstock.setText(rs.getString(5));
            }else if (rs ==null) {
                Toast.makeText(this,"Error de datos: El producto no existe",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void clickProducto(View view){
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_Codigo:
                if (checked) {
                    consultar_Prod("id_producto");
                    checkbox_Nombre.setChecked(false);}


                else
                    // Remove the meat
                    break;
            case R.id.checkbox_Nombre:
                if (checked) {
                    consultar_Prod("nombre_pro");

                    checkbox_Codigo.setChecked(false);}
                else
                    // I'm lactose intolerant
                    break;
                // TODO: Veggie sandwich
        }
    }


}