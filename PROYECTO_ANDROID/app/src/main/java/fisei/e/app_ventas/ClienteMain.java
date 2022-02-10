package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClienteMain extends AppCompatActivity {

    EditText etNombreCedula;

    TextView textViewCedula;
    TextView textViewNombre;
    TextView textViewApellido;
    TextView textViewDireccion;

    CheckBox checkBoxNombre;
    CheckBox checkBoxCedula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_main);

        etNombreCedula=(EditText) findViewById(R.id.editTextBusqueda);
        textViewCedula=(TextView) findViewById(R.id.textViewCedula);
        textViewNombre=(TextView)findViewById(R.id.textViewNombre);
        textViewApellido=(TextView) findViewById(R.id.textViewApellido);
        textViewDireccion=(TextView) findViewById(R.id.textViewDireccion);
        checkBoxNombre=(CheckBox) findViewById(R.id.checkbox_Nombre);
        checkBoxCedula=(CheckBox) findViewById(R.id.checkbox_Codigo);

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

    public void consultarCliente(String cadena){
        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select * from Clientes where "+cadena+" like'%"+etNombreCedula.getText()+"%'");


            if(rs.next()){
                textViewCedula.setText(rs.getString(2));
                textViewNombre.setText(rs.getString(3));
                textViewApellido.setText(rs.getString(4));
                textViewDireccion.setText(rs.getString(5));

            }else if (!rs.next()) {
                Toast.makeText(this,"Error de datos, el cliente no existe",Toast.LENGTH_SHORT);
            }


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void UtilizarCheck(View view){
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_Codigo:
                if (checked) {
                    checkBoxNombre.setChecked(false);
                    consultarCliente("cedula_cli");}
                else
                    // Remove the meat
                    break;
            case R.id.checkbox_Nombre:
                if (checked) {
                    checkBoxCedula.setChecked(false);
                    consultarCliente("nombre_cli");}

                else
                    // I'm lactose intolerant
                    break;
                // TODO: Veggie sandwich
        }
    }

    public void ClickNombreCedula(View view){
        etNombreCedula.setText("");
        checkBoxCedula.setChecked(false);
        checkBoxNombre.setChecked(false);
        textViewCedula.setText("");
        textViewNombre.setText("");
        textViewApellido.setText("");
        textViewDireccion.setText("");
    }


}