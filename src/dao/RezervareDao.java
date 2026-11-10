package dao;

import java.sql.*;
import java.time.LocalDate;

public class RezervareDao {
    private final Connection conn;

    public RezervareDao(Connection conn) {
        this.conn = conn;
    }

    public int insert(int idProprietate, int idClient, LocalDate checkIn, LocalDate checkOut, double pretTotal) throws SQLException {
        String sql = """
            INSERT INTO rezervare (id_proprietate, id_client, data_check_in, data_check_out, pret_total)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id_rezervare
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProprietate);
            ps.setInt(2, idClient);
            ps.setDate(3, Date.valueOf(checkIn));
            ps.setDate(4, Date.valueOf(checkOut));
            ps.setBigDecimal(5, java.math.BigDecimal.valueOf(pretTotal));

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
