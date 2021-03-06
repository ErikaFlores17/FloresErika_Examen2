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
    EditText editTextCorreo;
    int x=0;
    int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse_main);

        editTextCedula=(EditText) findViewById(R.id.editTextCedula);
        editTextNombre=(EditText) findViewById(R.id.editTextNombre);
        editTextApellido=(EditText) findViewById(R.id.editTextApellido);
        editTextDireccion=(EditText) findViewById(R.id.editTextDireccion);
        editTextClave=(EditText) findViewById(R.id.editTextClave);
        editTextCorreo=(EditText) findViewById(R.id.editTextCorreo);

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
        String correo= editTextCorreo.getText().toString();
        if (!cedula.equals("") && !nombre.equals("") && !apellido.equals("") && !direccion.equals("") && !clave.equals("") && !correo.equals("")) {
            ELFCHverificarCedula(cedula);
            if(cedulaCorrecta == true){
                if(clave.length()>=6 && clave.length()<=10) {
                    ELFCHverificarClave(clave);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "La longitud de la clave debe estar entre 6 y 10 caracteres maximo", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getApplicationContext(), "Cedula incorrecta", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Error Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    //VALIDAR CEDULA
    boolean cedulaCorrecta = false;

    public boolean ELFCHverificarCedula(String cedula) {

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
// Coeficientes de validaci??n c??dula
// El decimo digito se lo considera d??gito verificador
                    int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                    int verificador = Integer.parseInt(cedula.substring(9,10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    }
                    else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            System.out.println("La C??dula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }
    //VALIDAR CLAVE UNA MAYUSCULA UNA MINUSCULA UN NUMERO Y UN CARACTER
    public void ELFCHverificarClave(String claveV) {
        //1 mayuscula, 1 minuscula, 1 numero minimo
        //String password = "Cristian199";
        char clave;
        byte  contNumero = 0, contLetraMay = 0, contLetraMin=0,contCar=0;

        for (byte i = 0; i < claveV.length(); i++) {
            clave = claveV.charAt(i);
            String passValue = String.valueOf(clave);
            if (passValue.matches("[A-Z]")) {
                contLetraMay++;
            } else if (passValue.matches("[a-z]")) {
                contLetraMin++;
            } else if (passValue.matches("[0-9]")) {
                contNumero++;
            } else if (passValue.matches(",.*/-+")) {
            contCar++;
            }
        }

        if(contLetraMay==0 || contLetraMin==0 || contNumero==0){
            Toast.makeText(getApplicationContext(), "Contrase??a debil debe contener 1 numero,1 caracter,1May, 1Min minimo", Toast.LENGTH_SHORT).show();
        }
        else {
            InsertarCliente();

        }
    }


    //



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
        // CONSULTA PARA VERIFICAR QUE EL CORREO NO SE REPITA
        try {
            //CONSULTA PARA VER SI NO EXISTE EL CLIENTE
            Statement st = connectionclass().createStatement();
            ResultSet rs= st.executeQuery("select correo from Clientes where correo='"+editTextCorreo.getText()+"'");
            if(rs.next()) {
                if (rs.getString(1) != "") {
                    Toast.makeText(this, "El correo ya se encuentra registrado...", Toast.LENGTH_LONG).show();
                    y=1;
                }
            }
        }catch(SQLException e){
            Toast.makeText(getApplicationContext(),"error existe",Toast.LENGTH_SHORT).show();
        }
    }

    public void InsertarCliente(){
        ExisteCliente();
        if(x==0 && y==0) {
            Connection connection = connectionclass();
            try {
                if (connection != null) {
                    String sqlinsert = "Insert into Clientes values ('" + editTextCedula.getText().toString() + "','" + editTextNombre.getText().toString() + "','" + editTextApellido.getText().toString() + "','" + editTextDireccion.getText().toString() + "','" + editTextClave.getText().toString() + "','" + editTextCorreo.getText().toString() + "')";
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(sqlinsert);
                }
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Cliente guardado...", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(this, ELFCHMainActivity.class);
                    startActivity(intent);
            }
        }
        else{
            x=0;
            y=0;
           // Toast.makeText(getApplicationContext(), "ijiijij", Toast.LENGTH_SHORT).show();
        }

    }
}