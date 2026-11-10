package dao;
import java.sql.ResultSet;
import java.sql.Connection;
import model.Client;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ClientDao {
    private final Connection conn;

    public ClientDao(Connection conn) {
        this.conn = conn;
    }

    public int insert(Client c) throws SQLException {
        String sql = """
            INSERT INTO client (nume, email, telefon)
            VALUES (?, ?, ?)
            RETURNING id_client
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNume());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getTelefon());

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}