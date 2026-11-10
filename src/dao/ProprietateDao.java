package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietateDao {
    private final Connection conn;

    public ProprietateDao(Connection conn) {
        this.conn = conn;
    }

    public int insert(Proprietate p) throws SQLException {
        String sqlProp = """
        INSERT INTO proprietate (nume, adresa, tip_proprietate, pret_noapte, rating, nr_camere, are_bucatarie)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        RETURNING id_proprietate
    """;

        String sqlFac = """
        INSERT INTO facilitati (are_parcare, pret_parcare, are_mic_dejun, pret_mic_dejun, are_piscina, pret_piscina)
        VALUES (?, ?, ?, ?, ?, ?)
        RETURNING id_facilitati
    """;

        String sqlLink = """
        INSERT INTO proprietate_facilitati (id_proprietate, id_facilitati)
        VALUES (?, ?)
    """;

        try {
            conn.setAutoCommit(false);

            int idProp;
            try (PreparedStatement ps = conn.prepareStatement(sqlProp)) {
                ps.setString(1, p.getNume());
                ps.setString(2, p.getAdresa());
                ps.setString(3, p.getClass().getSimpleName());
                ps.setDouble(4, p.getPretNoapte());
                ps.setDouble(5, p.getRating());
                ps.setInt(6, p.getNrCamere());
                ps.setBoolean(7, p.isAreBucatarie());

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    idProp = rs.getInt(1);
                }
            }


            boolean areParcare = p.isAreParcare();
            double pretParcare = p.getPretParcare();
            boolean areMicDejun = p.isAreMicDejun();
            double pretMicDejun = p.getPretMicDejun();

            boolean arePiscina = false;
            double pretPiscina = 0.0;

            if (p instanceof Vila v) {
                arePiscina = v.isArePiscina();
                pretPiscina = v.getTaxaPiscina();
            } else if (p instanceof Cabana c) {
                arePiscina = c.isArePiscina();
                pretPiscina = c.getTaxaPiscina(); // sau ce getter ai tu
            }

            int idFac;
            try (PreparedStatement ps = conn.prepareStatement(sqlFac)) {
                ps.setBoolean(1, areParcare);
                ps.setDouble(2, pretParcare);
                ps.setBoolean(3, areMicDejun);
                ps.setDouble(4, pretMicDejun);
                ps.setBoolean(5, arePiscina);
                ps.setDouble(6, pretPiscina);

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    idFac = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlLink)) {
                ps.setInt(1, idProp);
                ps.setInt(2, idFac);
                ps.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return idProp;

        } catch (SQLException ex) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw ex;
        }
    }

    public List<Proprietate> findAll() throws SQLException {
        String sql = """
            SELECT p.id_proprietate, p.nume, p.adresa, p.tip_proprietate, p.pret_noapte, p.rating,
                   p.nr_camere, p.are_bucatarie,
                   f.are_parcare, f.pret_parcare, f.are_mic_dejun, f.pret_mic_dejun,
                   f.are_piscina, f.pret_piscina
            FROM proprietate p
            JOIN proprietate_facilitati pf ON pf.id_proprietate = p.id_proprietate
            JOIN facilitati f ON f.id_facilitati = pf.id_facilitati
        """;

        List<Proprietate> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_proprietate");
                String nume = rs.getString("nume");
                String adresa = rs.getString("adresa");
                String tip = rs.getString("tip_proprietate");
                double pretNoapte = rs.getDouble("pret_noapte");
                double rating = rs.getDouble("rating");
                int nrCamere = rs.getInt("nr_camere");
                boolean areBucatarie = rs.getBoolean("are_bucatarie");

                boolean areParcare = rs.getBoolean("are_parcare");
                double pretParcare = rs.getDouble("pret_parcare");
                boolean areMicDejun = rs.getBoolean("are_mic_dejun");
                double pretMicDejun = rs.getDouble("pret_mic_dejun");

                boolean arePiscina = rs.getBoolean("are_piscina");
                double pretPiscina = rs.getDouble("pret_piscina");

                Proprietate prop;

                if ("Vila".equalsIgnoreCase(tip)) {
                    prop = new Vila(id, nume, adresa, pretNoapte, rating,
                            areParcare, areMicDejun, pretMicDejun, pretParcare,
                            nrCamere, areBucatarie, arePiscina, pretPiscina);

                } else if ("Apartament".equalsIgnoreCase(tip)) {
                    prop = new Apartament(id, nume, adresa, pretNoapte, rating,
                            areParcare, areMicDejun, pretMicDejun, pretParcare,
                            nrCamere, areBucatarie, 0.0);

                } else {

                    prop = new Apartament(id, nume, adresa, pretNoapte, rating,
                            areParcare, areMicDejun, pretMicDejun, pretParcare,
                            nrCamere, areBucatarie, 0.0);
                }

                list.add(prop);
            }
        }

        return list;
    }
}
