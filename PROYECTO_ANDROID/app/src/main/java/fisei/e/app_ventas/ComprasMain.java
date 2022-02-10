package fisei.e.app_ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ComprasMain extends AppCompatActivity {
    Double auxiliarsum=0.0;
    EditText editTextIdBuscarProducto;
    TextView textViewNombreProducto;
    TextView textViewCodigoProducto;
    TextView textViewPrecioUnitario;
    TextView textViewCostoProducto;
    TextView textViewStock;
    TextView textViewFecha;
    TextView textViewCedula;

    TextView textViewNumeroFactura;
    ImageButton compra;

    CheckBox checkBoxNombreProducto;
    CheckBox checkBoxCodigoProducto;
    EditText cantidad;
    EditText usuario;
    String cedula;
    String idCliente;
    int contador =0;
    ArrayList<String> lstProductos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compras_main);
        editTextIdBuscarProducto=(EditText) findViewById(R.id.editTextBuscarProducto);
        textViewNombreProducto=(TextView) findViewById(R.id.textViewNombreProducto);
        textViewCodigoProducto=(TextView) findViewById(R.id.textViewCodigoProducto);
        textViewNumeroFactura = findViewById(R.id.textViewNUmeroFactura);
        textViewCostoProducto=(TextView) findViewById(R.id.textViewCostoProducto);
        textViewStock =(TextView) findViewById(R.id.textViewStock);
        textViewFecha =(TextView) findViewById(R.id.textViewfecha);
        textViewCedula = findViewById(R.id.textView_cedula);
        checkBoxCodigoProducto=(CheckBox)findViewById(R.id.checkboxCodigoProdcto);
        checkBoxNombreProducto=(CheckBox)findViewById(R.id.checkboxNombreProducto);
        cantidad = findViewById(R.id.editextCantidad);


        compra= (ImageButton) findViewById(R.id.btnCompra);
        cedula = getIntent().getExtras().getString("cedula");
        NUmerofactura();
        CargarCliente();
        //fecha
        //Date date = new Date(); //sql
        Date date = new Date();
        SimpleDateFormat fechaC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String sFecha = fechaC.format(date);
        textViewFecha.setText(sFecha);
        textViewCedula.setText(cedula);


    }

    private void CargarCliente() {

        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select id_cliente from Clientes where cedula_cli= '"+ cedula +"'");
            if (rs.next()){
                idCliente = rs.getString(1);

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private void NUmerofactura() {

        try {
            Statement st = conexionDB().createStatement();

            ResultSet rs= st.executeQuery("select id_venta from Venta");
            while(rs.next()){
                contador++;
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        textViewNumeroFactura.setText(String.valueOf(contador+1));
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


    public void consultarProducto(String cadena){
        try {
            Statement st = conexionDB().createStatement();
            ResultSet rs= st.executeQuery("select * from Productos where "+cadena+" like'%"+editTextIdBuscarProducto.getText()+"%'");
            if(rs.next()){
                textViewCodigoProducto.setText(rs.getString(1));
                textViewNombreProducto.setText(rs.getString(2));
                // textViewPrecioUnitario.setText(rs.getString(3));
                textViewCostoProducto.setText(rs.getString(4));
                textViewStock.setText(rs.getString(5));
            }else if (rs ==null) {
                Toast.makeText(this,"Error de datos, el producto no existe",Toast.LENGTH_LONG);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }




    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkboxCodigoProdcto:
                if (checked) {
                    consultarProducto("id_producto");
                    checkBoxNombreProducto.setChecked(false);}
                else
                    // Remove the meat
                    break;
            case R.id.checkboxNombreProducto:
                if (checked) {

                    consultarProducto("nombre_pro");
                    checkBoxCodigoProducto.setChecked(false);}


                else
                    // I'm lactose intolerant
                    break;
                // TODO: Veggie sandwich
        }
        cantidad.setText("");
    }

    public void onClickTextView(View view){
        editTextIdBuscarProducto.setText("");
        checkBoxNombreProducto.setChecked(false);
        checkBoxCodigoProducto.setChecked(false);
    }


    public void VerProductos(View view){
        Intent intent = new Intent(this, ListaProductosMain.class);
        //si queremos que la actividad regrese datos hacia atras
        startActivity(intent);
    }


    public void InsertarVenta(View view){
        try {
            String cant=cantidad.getText().toString();
            int cant1=Integer.parseInt(cant);
            String stock=textViewStock.getText().toString();
            int stock1=Integer.parseInt(stock);
            if (!cant.equals("")) {
            }else{
                Toast.makeText(getApplicationContext(), "La cantidad no puede ser vacio ", Toast.LENGTH_SHORT).show();
            }
                if ((cant1 != 0) && (cant1 <= stock1)) {
                    llenarLista();
                    if(lstProductos.size()==1){
                        AgregarVenta();
                    }
                    AgregarDetalle();
                } else {
                    Toast.makeText(getApplicationContext(), "La cantidad es incorrecta ", Toast.LENGTH_SHORT).show();
                }

        }catch(Exception e){

        }
    }

    private void llenarLista() {
        try {
            String  preciov = textViewCostoProducto.getText().toString();
            String cant =  cantidad.getText().toString();
            Double x = Double.parseDouble(preciov);
            int y = Integer.parseInt(cant);
            Double subtotal = x * y;
            Double iva = (subtotal * 12)/100;
            Double total = ( x * y)  + ((subtotal * 12)/100);

            lstProductos.add(textViewNombreProducto.getText().toString()+"              " +
                    cantidad.getText().toString() +"              "
                    +textViewCostoProducto.getText().toString()+"              "
                    + String.valueOf(total));


            Intent intent = new Intent(this,FacturacionActivity.class);
            // intent.putExtra("NombrePro",textViewNombreProducto.getText().toString());
            intent.putStringArrayListExtra("lstProductos",lstProductos);

            String nf=textViewNumeroFactura.getText().toString();
            intent.putExtra("numerof",nf);
            startActivity(intent);

        } catch(Exception e){


        }


    }

    public void AgregarVenta(){

        String  preciov = textViewCostoProducto.getText().toString();
        String stock = textViewStock.getText().toString();
        String cant =  cantidad.getText().toString();
        Connection connection = connectionclass();


        Double x = Double.parseDouble(preciov);
        int y = Integer.parseInt(cant);

        Double subtotal = x * y;
        Double iva = (subtotal * 12)/100;
        Double total = ( x * y)  + ((subtotal * 12)/100);

        try {
            if(connection !=null){

                String sqlinsert = "Insert into Venta values ('" +
                        textViewFecha.getText().toString() + "','" +
                        idCliente + "','" +
                        total + "','" +
                        subtotal + "','" +
                        iva + "')";

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlinsert);
                Toast.makeText(this, "Insertado", Toast.LENGTH_LONG).show();
            }

        }catch(SQLException e){
           // Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
           // Toast.makeText(getApplicationContext(),"ingresado",Toast.LENGTH_SHORT).show();
        }
    }


    public void AgregarDetalle(){
        /////////////////////////////

        String  preciov = textViewCostoProducto.getText().toString();
        String stock = textViewStock.getText().toString();
        String cant =  cantidad.getText().toString();
        Connection connection = connectionclass();

        Double a = Double.parseDouble(preciov);
        int b = Integer.parseInt(cant);

        Double tot = a * b;
        auxiliarsum=auxiliarsum+tot;
        Double auxiva=auxiliarsum*0.12;
        Double auxsubtotal=auxiliarsum-auxiva;
        Actualizar(auxiliarsum,auxiva,auxsubtotal);
        //Toast.makeText(this, auxiliarsum.toString(), Toast.LENGTH_LONG).show();
        // textView8.setText(String.valueOf(tot));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        try {
            if(connection !=null){

                int numeroFac = contador+1;

                String sqlinsert = "Insert into DetalleVenta values ('" +
                        numeroFac+"','"+
                        textViewCodigoProducto.getText().toString() + "','" +
                        cantidad.getText().toString() + "','" +
                        textViewCostoProducto.getText().toString()+ "','" +
                        tot + "')";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlinsert);
                Toast.makeText(this, "Insertado", Toast.LENGTH_LONG).show();
            }

        }catch(SQLException e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Ingresado",Toast.LENGTH_SHORT).show();
        }
    }

    public void Actualizar(Double val,Double iva, Double subtotal){
        try {
            Statement st = conexionDB().createStatement();
            int numf=contador+1;
            //ResultSet rs= st.executeQuery("update Venta set total="+val+"where id_venta="+numf);
            ResultSet rs= st.executeQuery("update Venta set total="+val+","+"iva="+iva+","+"subtotal="+subtotal+"where id_venta="+numf);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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


}