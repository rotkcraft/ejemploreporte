package paquete;

import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Reporte
{
    private String url = "jdbc:hsqldb:file:bd/bdprueba";
    private String user = "root";
    private String pass = "root";
    private JasperDesign jasperDesign;
    private JasperPrint jasperPrint;
    private JasperReport jasperReport;
    private JRViewer jasperViewer;

    public Reporte(String reporte)
    {
        try
        {
            Connection conexion = DriverManager.getConnection(url, user, pass);
            jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream(reporte));
            jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> param = new HashMap<String, Object>();


            for (JRParameter parametro : jasperReport.getParameters())
            {
                if (!parametro.isSystemDefined() && parametro.isForPrompting())
                {
                    System.out.println(parametro.getName());
                    System.out.println(parametro.getDescription());
                    param.put(parametro.getName(), valor(parametro.getValueClass().getSimpleName(), parametro.getDescription()));
                }
            }


            jasperPrint = JasperFillManager.fillReport(jasperReport, param, conexion);
            jasperViewer = new JRViewer(jasperPrint);

            conexion.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public JRViewer getJasperViewer()
    {
        return jasperViewer;
    }


    private Object valor(String simpleName, String parametro)
    {

        if (simpleName.equals("String"))
        {
            return JOptionPane.showInputDialog(null, "Ingrese " + parametro);
        }
        else if (simpleName.equals("Date"))
        {

            final JDialog dialogo = new JDialog();
            dialogo.setLayout(new FlowLayout(FlowLayout.LEFT));
            final CountDownLatch espera = new CountDownLatch(1);


            JDateChooser calendario = new JDateChooser();
            JButton boton = new JButton("Seleccionar");

            ((JTextField) calendario.getComponent(1)).setEditable(false);
            ((JTextField) calendario.getComponent(1)).setColumns(20);
            calendario.setDateFormatString("yyyy-MM-dd");

            dialogo.add(new JLabel("Ingrese " + parametro));
            dialogo.add(calendario);

            boton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    espera.countDown();
                    dialogo.dispose();
                }
            });
            dialogo.add(boton);
            dialogo.pack();
            dialogo.setVisible(true);
            dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

            try
            {
                espera.await();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            return calendario.getDate();

        }
        return null;
    }

}
