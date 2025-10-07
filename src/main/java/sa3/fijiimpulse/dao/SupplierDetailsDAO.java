package sa3.fijiimpulse.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.SupplierDetails;

import java.util.List;

@Repository
public class SupplierDetailsDAO {
    private final JdbcTemplate jdbcTemplate;

    public SupplierDetailsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SupplierDetails> findAll() {
        String sql = "SELECT * FROM SUPPLIER_DETAILS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new SupplierDetails(
                rs.getInt("Supplier_id"),
                rs.getString("Company_name"),
                rs.getString("Phone_number"),
                rs.getString("Supplier_email")
        ));
    }

    public SupplierDetails findById(int id) {
        String sql = "SELECT * FROM SUPPLIER_DETAILS WHERE Supplier_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new SupplierDetails(
                rs.getInt("Supplier_id"),
                rs.getString("Company_name"),
                rs.getString("Phone_number"),
                rs.getString("Supplier_email")
        ));
    }

    public int insert(SupplierDetails s) {
        String sql = "INSERT INTO SUPPLIER_DETAILS (Company_name, Phone_number, Supplier_email) VALUES (?,?,?)";
        return jdbcTemplate.update(sql, s.getCompanyName(), s.getPhoneNumber(), s.getSupplierEmail());
    }

    public int update(SupplierDetails s) {
        String sql = "UPDATE SUPPLIER_DETAILS SET Company_name=?, Phone_number=?, Supplier_email=? WHERE Supplier_id=?";
        return jdbcTemplate.update(sql, s.getCompanyName(), s.getPhoneNumber(), s.getSupplierEmail(), s.getSupplierId());
    }

    public int delete(int id) {
        String sql = "DELETE FROM SUPPLIER_DETAILS WHERE Supplier_id=?";
        return jdbcTemplate.update(sql, id);
    }
}
