package dao;

import java.sql.*;
import java.time.LocalDate;

public class PlataDao {
    private final Connection conn;

    public PlataDao(Connection conn) {
        this.conn = conn;
    }

    public void insert(int idRezervare, double suma, LocalDate dataPlata, String metoda) throws SQLException {
        String sql = """
            INSERT INTO plata (id_rezervare, suma, data_plata, metoda_plata)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRezervare);
            ps.setBigDecimal(2, java.math.BigDecimal.valueOf(suma));
            ps.setDate(3, Date.valueOf(dataPlata));
            ps.setString(4, metoda);
            ps.executeUpdate();
        }
    }
}

