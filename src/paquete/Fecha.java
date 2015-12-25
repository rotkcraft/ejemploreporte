package paquete;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creado por hadexexplade el 30/07/15.18:40
 */
public class Fecha
{
    @Test
    public void metodo()
    {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");


        Date dt = new Date();
        System.out.println("dt = " + dt);
        System.out.println("df.format(new Date()) = " + df.format(dt));
    }
}