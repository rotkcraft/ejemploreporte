package paquete;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class Conexion
{

    private String url;
    private String usuario;
    private String clave;

    public Conexion()
    {
        this.usuario = "root";
        this.clave = "root";
        this.url = "jdbc:hsqldb:file:bd/bdprueba";
    }

    public Conexion(String url, String usuario, String clave)
    {
        this.url = "jdbc:mysql://localhost:3306/" + url;
        this.usuario = usuario;
        this.clave = clave;
    }

    public Connection getConnection()
    {
        Connection conec = null;

        try
        {
            conec = DriverManager.getConnection(url, usuario, clave);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return conec;
    }

    public void sqlQuery(String sql) // Para Realizar sentecnias como insert
    {
        Connection conn = null;
        Statement st = null;

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            st.execute(sql);
            st.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int sqlQueryArchivo(String sql, String rutaArchivo, int campo) // Mete un archivo ala base de datos
    {
        InputStream entrada = null;
        Connection conexion = null;
        PreparedStatement ps = null;
        int ingresados = 0;

        try
        {
            File archivo;

            conexion = DriverManager.getConnection(url, usuario, clave);
            conexion.setAutoCommit(false);

            ps = conexion.prepareStatement(sql);

            archivo = new File(rutaArchivo);
            entrada = new FileInputStream(archivo);

            ps.setBinaryStream(campo, entrada, (int) archivo.length());

            ps.executeUpdate();
            conexion.commit();
            entrada.close();
            ps.close();
            conexion.close();

        }
        catch (FileNotFoundException ex)
        {
        }
        catch (IOException ex)
        {
        }
        catch (SQLException ex)
        {
        }

        return ingresados;
    }

    public String sqlQueryResultSet(String sql)
    {
        String retorno = "";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next())
            {
                retorno = rs.getString(1);
            }

            rs.close();
            st.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return retorno;
    }

    public String valor(String sql)
    {

        String retorno = "";

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rd;

        try
        {

            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rd = rs.getMetaData();

            while (rs.next())
            {
                for (int i = 1; i <= rd.getColumnCount(); i++)
                {
                    retorno += rs.getString(i) + " ";
                }
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retorno;
    }

    public String obtenerCampo(String campoobtener, String tabla, String buscarpor, String busquedad)
    {
        String retorno = "";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT " + campoobtener + " FROM " + tabla + " WHERE " + buscarpor + " = '" + busquedad + "'");

            while (rs.next())
            {
                retorno = rs.getString(1);
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return retorno;
    }

    public int aumentarID(String sql) // Aumenta autmaticamente el id
    {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        int valor = 0;

        try
        {
            con = DriverManager.getConnection(url, usuario, clave);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next())
            {
                valor = rs.getInt(1);
            }

            valor += 1;

            rs.close();
            st.close();
            con.close();

        }
        catch (Exception e)
        {

        }

        return valor;
    }

    public int cantidadColumnas(String sql) // Cuenta columnas de la tabla
    {
        int retorno = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            retorno = rs.getMetaData().getColumnCount();

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retorno;
    }

    public int cantidadFilas(String sql) // Cuenta la filas que hay dentro de la consulta
    {
        int retorno = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next())
            {
                retorno = rs.getRow();
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retorno;
    }

    public ObservableList<TableColumn> ColumnasTabla(String sql) // Obtener Titulos de las columnas
    {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rd;

        ObservableList<TableColumn> retornar = FXCollections.observableArrayList();

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rd = rs.getMetaData();

            for (int i = 1; i <= rd.getColumnCount(); i++)
            {
                retornar.add(new TableColumn(rd.getColumnLabel(i)));
            }

            rs.close();
            st.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retornar;
    }

    public ObservableList Columnas(String sql) // Obtener Titulos de las columnas
    {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData rd;

        ObservableList retornar = FXCollections.observableArrayList();

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            rd = rs.getMetaData();

            for (int i = 1; i <= rd.getColumnCount(); i++)
            {
                retornar.add(rd.getColumnName(i));
            }

            rs.close();
            st.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retornar;
    }


    public ObservableList<ObservableList> datos(String sql) // debuelbe todos los registros de la tabla
    {
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rmd;
        Blob blob;

        ObservableList<ObservableList> datos = FXCollections.observableArrayList();

        try
        {
            con = DriverManager.getConnection(url, usuario, clave);

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            rmd = rs.getMetaData();

            while (rs.next())
            {

                ObservableList<Object> fila = FXCollections.observableArrayList();

                for (int x = 1; x <= rmd.getColumnCount(); x++)
                {
                    if (rmd.getColumnTypeName(x).equalsIgnoreCase("blob") || rmd.getColumnTypeName(x).equalsIgnoreCase("longblob"))
                    {
                        Button Boton = new Button();

                        blob = rs.getBlob(x);

                        byte[] Archivo = null;

                        if (blob.length() > 0)
                        {
                            Archivo = blob.getBytes(1, (int) blob.length());


                            final byte[] Ima = Archivo;

                            Boton.setGraphic(new ImageView(new Image(new ByteArrayInputStream(Archivo), 50, 90, true, true)));

                            fila.add(Boton);

                            fila.add(Boton);

                        }

                    }
                    else
                    {
                        fila.add(rs.getString(x));
                    }

                }

                datos.add(fila);
            }


            rs.close();
            ps.close();
            con.close();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return datos;
    }

    public int obtenerValor(String sql)
    {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        int valor = 0;

        try
        {
            con = DriverManager.getConnection(url, usuario, clave);
            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next())
            {
                valor = rs.getInt(1);
            }

            rs.close();
            st.close();
            con.close();

        }
        catch (Exception e)
        {

        }

        return valor;
    }

    public void llenarComboBox(String sql, ComboBox ComboBox, String ingresar)
    {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        ComboBox.getItems().clear();

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            ComboBox.getItems().add("");

            while (rs.next())
            {
                ComboBox.getItems().add(rs.getString(1));
            }

            if (ingresar.length() > 1)
            {
                ComboBox.getItems().add(ingresar);
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void llenarmodelComboColumnas(String sql, ComboBox ComboBox)
    {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        ResultSetMetaData md;

        ComboBox.getItems().clear();

        try
        {
            conn = DriverManager.getConnection(url, usuario, clave);
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            md = rs.getMetaData();

            ComboBox.getItems().add("");

            for (int i = 1; i < md.getColumnCount(); i++)
            {
                ComboBox.getItems().add(md.getColumnLabel(i));
            }

            rs.close();
            st.close();
            conn.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void llenarTabla(String SQL, TableView Tabla)
    {
        Tabla.getColumns().addAll(ColumnasTabla(SQL));
        for (int i = 1; i <= Tabla.getColumns().size(); i++)
        {

            final int x = i - 1;

            ((TableColumn) Tabla.getColumns().get(x)).setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Object>, ObservableValue>()
            {

                public ObservableValue<Object> call(TableColumn.CellDataFeatures<ObservableList, Object> param)
                {
                    return new SimpleObjectProperty<Object>(param.getValue().get(x));
                }
            });

        }
        Tabla.getItems().addAll(datos(SQL));
    }

    public ImageIcon obtenerArchivo(String sql) // Obtiene Un archivo de una base de datos 
    {
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Blob blob;
        ImageIcon imagen = null;

        try
        {

            int campoB = 0;

            conexion = DriverManager.getConnection(url, usuario, clave);
            conexion.setAutoCommit(false);
            ps = conexion.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next())
            {
                blob = rs.getBlob(campoB);
                imagen = new ImageIcon(blob.getBytes(1, (int) blob.length()));
            }

            conexion.commit();
            rs.close();
            ps.close();
            conexion.close();

        }
        catch (SQLException ex)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);

        }
        return imagen;
    }


    public void ingresarURL(String sql, String ruta)
    {
        Connection con = null;
        PreparedStatement st = null;

        try
        {
            con = DriverManager.getConnection(url, usuario, clave);
            st = con.prepareStatement(sql);
            st.setString(1, ruta);
            st.executeUpdate();

            st.close();
            con.close();

        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

}
