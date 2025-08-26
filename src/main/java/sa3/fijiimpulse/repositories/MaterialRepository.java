package sa3.fijiimpulse.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sa3.fijiimpulse.entity.Material;

import java.util.List;

@Repository
public class MaterialRepository {
    private final JdbcTemplate jdbcTemplate;

    public MaterialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Material m) {
        String sql = "INSERT INTO materials (material_name, quantity, material_status, last_updated, supplier_id) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, m.getMaterialName(), m.getQuantity(), m.getMaterialStatus(), m.getLastUpdated(), m.getSupplierId());
    }

    public int update(Material m) {
        String sql = "UPDATE materials SET material_name=?, quantity=?, material_status=?, last_updated=?, supplier_id=? WHERE material_id=?";
        return jdbcTemplate.update(sql, m.getMaterialName(), m.getQuantity(), m.getMaterialStatus(), m.getLastUpdated(), m.getSupplierId(), m.getMaterialId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM materials WHERE material_id=?";
        return jdbcTemplate.update(sql, id);
    }

    public Material findById(Long id) {
        String sql = "SELECT * FROM materials WHERE material_id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Material(rs.getLong("material_id"),
                        rs.getString("material_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getString("material_status"),
                        rs.getTimestamp("last_updated").toLocalDateTime(),
                        rs.getLong("supplier_id")), id);
    }

    public List<Material> findAll() {
        String sql = "SELECT * FROM materials";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Material(rs.getLong("material_id"),
                        rs.getString("material_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getString("material_status"),
                        rs.getTimestamp("last_updated").toLocalDateTime(),
                        rs.getLong("supplier_id")));
    }
}
