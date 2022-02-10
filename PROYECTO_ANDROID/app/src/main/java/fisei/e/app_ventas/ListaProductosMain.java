package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListaProductosMain extends AppCompatActivity {
    ArrayList<String> list = null;
    ListView listviewProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos_main);
        listviewProducto= findViewById(R.id.listviewProducto);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, MostrarProducto());
        listviewProducto.setAdapter(adapter);

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

    public ArrayList<String> MostrarProducto(){

        try {

            Statement st = conexionDB().createStatement();
            ResultSet rs = st.executeQuery("select * from Productos" );
            list = new ArrayList<String>();

            while (rs.next()){
                list.add(rs.getString(1)+"                           "+
                        rs.getString(2)+"                            "+
                        //  rs.getString(3)+"                           "+
                        rs.getString(4));
                // rs.getString(5));
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return  list;
    }

}