package paquete;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Creado por rotkcraft el 20 de febrero del 2015 .
 */
public class Principal extends Application
{

    private Tabla tabla;
    private Conexion conectaBD;
    private SwingNode nodo;
    private ScrollPane panel;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        GridPane rejilla = new GridPane();
        rejilla.setPadding(new Insets(1, 1, 2, 1));

        Reporte reporte = new Reporte("/reportes/Flower_Table_Based.jrxml");

        nodo = new SwingNode();
        nodo.setContent(reporte.getJasperViewer());

        panel = new ScrollPane();
        panel.setPrefSize(400, 550);
        panel.setContent(nodo);
        panel.setFitToHeight(true);
        panel.setFitToWidth(true);
        rejilla.add(panel, 0, 0);

        tabla = new Tabla();
        conectaBD = new Conexion();
        conectaBD.llenarTabla("select * from cliente", tabla);
        rejilla.add(tabla, 0, 1);

        Scene scene = new Scene(rejilla);

        stage.setScene(scene);
        stage.show();
    }
}
