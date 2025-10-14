package sa3.fijiimpulse.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.ProductItem;

import java.util.List;

@Service
public class ProductItemService {

    @Autowired
    private ProductItemDAO productItemDAO;

    // Create
    public int save(ProductItem item) {
        return productItemDAO.save(item);
    }

    // Read one
    public ProductItem findBySerialNo(String serialNo) {
        return productItemDAO.findBySerialNo(serialNo);
    }

    // Read all
    public List<ProductItem> findAll() {
        return productItemDAO.findAll();
    }

    // Update order
    public int updateOrder(String serialNo, Long orderId) {
        return productItemDAO.updateOrder(serialNo, orderId);
    }

    // Delete
    public int delete(String serialNo) {
        return productItemDAO.delete(serialNo);
    }
}