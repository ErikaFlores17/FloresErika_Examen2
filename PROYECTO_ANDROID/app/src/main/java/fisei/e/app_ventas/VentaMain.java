package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VentaMain extends AppCompatActivity {
    EditText editTBusqueda;
    CheckBox checkboxIDVenta;
    TextView textviewDetalles;
    TextView textViewCedulaVenta;
    TextView tvNombreC;
    TextView textViewTelefonoVenta;
    TextView textViewFechaVenta;
    TextView textViewSubTotalVenta;
    TextView textViewTotalVenta;
    TextView textViewIvaVenta;
    String numerof;
    TextView textViewcedul;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta_main);

        editTBusqueda=(EditText) findViewById(R.id.editTBusqueda);
        checkboxIDVenta=(CheckBox) findViewById(R.id.checkboxIDVenta);

        textViewCedulaVenta=(TextView) findViewById(R.id.textViewCedulaVenta);
       tvNombreC=(TextView) findViewById(R.id.tvNombreC);
        textViewTelefonoVenta=(TextView) findViewById(R.id.textViewTelefonoVenta);
        textViewFechaVenta=(TextView) findViewById(R.id.textViewFechaVenta);
        textViewSubTotalVenta=(TextView) findViewById(R.id.textViewSubTotalVenta);
        textViewTotalVenta=(TextView) findViewById(R.id.textViewTotalVenta);
        textViewIvaVenta=(TextView) findViewById(R.id.textViewIvaVenta);
        textviewDetalles=(TextView) findViewById(R.id.textViewDetalle);
        textViewcedul=(TextView)findViewById(R.id.textViewcedul);

        numerof= getIntent().getExtras().getString("numerof");
        editTBusqueda.setText(numerof);
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
    public void consultarUsuario(){
        try {
            //CONSULTA PARA VER SI NO EXISTE EL CLIENTE
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select v.id_venta from Clientes c inner join Venta v on c.id_cliente = v.id_cliente where c.cedula_cli='"+textViewcedul.getText()+"'");
            if(rs.next()) {
                if (rs.getString(1) != "") {
                    String id=rs.getString(1);
                    if(id==editTBusqueda.getText().toString()){
                        //consultarVentas();
                        consultarVentas();
                        consultarConceptos();
                    }
                else
                    {
                        Toast.makeText(getApplicationContext(),"No existe registros",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }catch(SQLException e){
            Toast.makeText(getApplicationContext(),"error existe",Toast.LENGTH_SHORT).show();
        }

    }
    public void consultarVentas(){
        try {
            Statement st = conexionDB().createStatement();

            ResultSet rs= st.executeQuery("select c.cedula_cli,c.nombre_cli +' '+c.apellido_cli, c.direccion_cli, v.fecha_ven,v.total,v.subtotal,v.iva \n" +
                    "from Clientes  c\n" +
                    "inner join Venta v on\n" +
                    "c.id_cliente =v.id_cliente\n" +
                    "where v.id_venta ='"+editTBusqueda.getText()+"'");
            if(rs.next()){
                textViewCedulaVenta.setText(rs.getString(1));
                tvNombreC.setText(rs.getString(2));
                textViewTelefonoVenta.setText(rs.getString(3));
                textViewFechaVenta.setText(rs.getString(4));
                textViewTotalVenta.setText(rs.getString(5));
               textViewSubTotalVenta.setText(rs.getString(6));
              textViewIvaVenta.setText(rs.getString(7));


            }else if (rs ==null) {
                Toast.makeText(this,"Error de datos, la venta no existe",Toast.LENGTH_LONG);
            }


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    public void consultarConceptos(){
        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select  c.cantidad, p.nombre_pro,c.precio,c.total_deta\n" +
                    "from DetalleVenta c\n" +
                    " join Venta v on\n" +
                    "c.id_vent_per =v.id_venta\n" +
                    " join Productos p on\n" +
                    "p.id_producto =c.id_pro\n" +
                    "where v.id_venta ='"+editTBusqueda.getText()+"'");
            while (rs.next()==true) {


                for (int i = 1; i <= 4; i++) {

                    String datos = rs.getString(i);
                    textviewDetalles.append(datos + "        |         ");
                }

            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void checkFactura(View view){
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxIDVenta:
                if (checked) {
                    consultarUsuario();
                    //consultarVentas();
                    //consultarConceptos();
                }
                else
                    // Remove the meat
                    break;

        }
    }




}