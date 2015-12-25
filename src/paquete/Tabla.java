package paquete;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Creado por rotkcraft el 20 de febrero del 2015 .
 */
public class Tabla extends TableView
{
    private ObservableList<ObservableList> datos;

    public Tabla()
    {
        super();

        datos = FXCollections.observableArrayList();
    }

    public ObservableList<ObservableList> getDatos()
    {
        return datos;
    }

    public void setDatos(ObservableList<ObservableList> datos)
    {
        this.datos = datos;
    }
}
