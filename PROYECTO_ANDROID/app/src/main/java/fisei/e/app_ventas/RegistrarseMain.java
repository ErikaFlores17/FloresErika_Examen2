package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrarseMain extends AppCompatActivity {

    EditText editTextCedula;
    EditText editTextNombre;
    EditText editTextApellido;
    EditText editTextDireccion;
    EditText editTextClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_main);

        editTextCedula=(EditText) findViewById(R.id.editTextCedula);
        editTextNombre=(EditText) findViewById(R.id.editTextNombre);
        editTextApellido=(EditText) findViewById(R.id.editTextApellido);
        editTextDireccion=(EditText) findViewById(R.id.editTextDireccion);
        editTextClave=(EditText) findViewById(R.id.editTextClave);

    }
    @SuppressLint("NewApi")
    public Connection connectionclass(){
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



    public void ClickInsertar(View view){
        Connection connection = connectionclass();
        String cedula = editTextCedula.getText().toString();
        String nombre = editTextNombre.getText().toString();
        String apellido = editTextApellido.getText().toString();
        String direccion = editTextDireccion.getText().toString();
        String clave = editTextClave.getText().toString();
        if (!cedula.equals("") && !nombre.equals("") && !apellido.equals("") && !direccion.equals("") && !clave.equals("")) {
            InsertarCliente();
        } else {
            Toast.makeText(getApplicationContext(), "Error ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    int x=0;

    public void ExisteCliente(){
        try {
                    //CONSULTA PARA VER SI NO EXISTE EL CLIENTE
                    Statement st = connectionclass().createStatement();
                    ResultSet rs= st.executeQuery("select cedula_cli from Clientes where cedula_cli='"+editTextCedula.getText()+"'");
                    if(rs.next()) {
                        if (rs.getString(1) != "") {
                            Toast.makeText(this, "El cliente ya existe...", Toast.LENGTH_LONG).show();
                            x=1;
                        }
                    }
        }catch(SQLException e){
                Toast.makeText(getApplicationContext(),"error existe",Toast.LENGTH_SHORT).show();
        }
    }

    public void InsertarCliente(){

        ExisteCliente();
        if(x==0) {
            Connection connection = connectionclass();
            try {
                if (connection != null) {
                    String sqlinsert = "Insert into Clientes values ('" + editTextCedula.getText().toString() + "','" + editTextNombre.getText().toString() + "','" + editTextApellido.getText().toString() + "','" + editTextDireccion.getText().toString() + "','" + editTextClave.getText().toString() + "')";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlinsert);
                }
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Cliente Guardado...", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(this, ELFCHMainActivity.class);
                    startActivity(intent);

            }
        }
        else{
            x=0;
           // Toast.makeText(getApplicationContext(), "ijiijij", Toast.LENGTH_SHORT).show();
        }

    }
}