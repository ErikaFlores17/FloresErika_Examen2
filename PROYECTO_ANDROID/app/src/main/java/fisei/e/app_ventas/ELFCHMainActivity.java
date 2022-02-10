package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity  {
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonIngresar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);

        buttonIngresar1=(Button) findViewById(R.id.buttonIngresar);
        buttonIngresar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                consultarLogin();

            }
        });


    }
    public void registrarse (View view){
        editTextEmail.setText("");
        editTextPassword.setText("");
        Intent intent = new Intent(this, RegistrarseMain.class);
        startActivity(intent);
    }

//public void iniciar (View view){}

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
    public void consultarLogin(){
        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select cedula_cli from Clientes\n" +
                    "where cedula_cli='"+ editTextEmail.getText()+"'\n" +
                    "and contrasenia='"+ editTextPassword.getText()+"'");
            if(rs.next()){
                if (rs.getString(1 )!="") {

                        String cedula = editTextEmail.getText().toString();
                            editTextEmail.setText("");
                            editTextPassword.setText("");
                            Intent intent = new Intent(this, Menu.class);
                            intent.putExtra("cedula",cedula);
                            startActivity(intent);
                }
            }else if (rs.getString(1 )=="") {
                Toast.makeText(this,"Error Datos no encontrados",Toast.LENGTH_LONG).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Usuario o Contraseña no coinciden",Toast.LENGTH_SHORT).show();
        }
    }




}