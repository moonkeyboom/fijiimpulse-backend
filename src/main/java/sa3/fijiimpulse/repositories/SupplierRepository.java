package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Supplier;

import java.util.List;

@Repository
public class SupplierRepository {
    private final JdbcTemplate jdbcTemplate;

    public SupplierRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Supplier s) {
        String sql = "INSERT INTO suppliers (company_name, phone_number, supplier_email) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, s.getCompanyName(), s.getPhoneNumber(), s.getSupplierEmail());
    }

    public int update(Supplier s) {
        String sql = "UPDATE suppliers SET company_name=?, phone_number=?, supplier_email=? WHERE supplier_id=?";
        return jdbcTemplate.update(sql, s.getCompanyName(), s.getPhoneNumber(), s.getSupplierEmail(), s.getSupplierId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM suppliers WHERE supplier_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public Supplier findById(Long id) {
        String sql = "SELECT * FROM suppliers WHERE supplier_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Supplier(rs.getLong("supplier_id"),
                        rs.getString("company_name"),
                        rs.getString("phone_number"),
                        rs.getString("supplier_email")), id);
    }

    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Supplier(rs.getLong("supplier_id"),
                        rs.getString("company_name"),
                        rs.getString("phone_number"),
                        rs.getString("supplier_email")));
    }
}
