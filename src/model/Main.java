package model;
import dao.ClientDao;
import dao.ProprietateDao;
import dao.RezervareDao;
import dao.PlataDao;
import util.DatabaseConnection;
import javax.swing.*;
import java.sql.Connection;
public class Main {
    public static void main(String[] args) {

        BookingManager manager = new BookingManager();


        SwingUtilities.invokeLater(() -> {
            try {

                try {
                    Class.forName("org.postgresql.Driver");
                    System.out.println("Driver PostgreSQL încărcat!");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Connection conn = DatabaseConnection.getConnection();
                ClientDao clientDao = new ClientDao(conn);
                ProprietateDao proprietateDao = new ProprietateDao(conn);
                RezervareDao rezervareDao = new RezervareDao(conn);
                PlataDao plataDao = new PlataDao(conn);
                for (Proprietate p : proprietateDao.findAll()) {
                    manager.adauga(p);
                }
                BookingGUI gui = new BookingGUI(manager,clientDao,proprietateDao,rezervareDao,plataDao);
                gui.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }
}