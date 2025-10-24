package sa3.fijiimpulse.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sa3.fijiimpulse.dao.ProductItemDAO;
import sa3.fijiimpulse.entity.ProductItem;

import java.util.List;
import java.util.UUID;

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

    public List<ProductItem> getProductItemsByOrderId(long orderId) {
        return productItemDAO.findByOrderId(orderId);
    }


//    // เพิ่ม ProductItem เข้ากับ Order
//    public void addProductItemsToOrder(int modelId, long orderId, int quantity) {
//        // 1. ดึง ProductItem ที่ยังไม่ถูก assign (orderId = null)
//        List<ProductItem> availableItems = productItemDAO.findAvailableByModelId(modelId);
//
//        int availableCount = availableItems.size();
//
//        // 2. Assign เท่าที่มี
//        int assigned = 0;
//        for (ProductItem item : availableItems) {
//            if (assigned >= quantity) break;
//            productItemDAO.updateOrder(item.getSerialNo(), orderId);
//            assigned++;
//        }
//
//        // 3. ถ้าไม่พอ → ต้องสร้างเพิ่ม
//        int remaining = quantity - assigned;
//        for (int i = 0; i < remaining; i++) {
//            ProductItem newItem = new ProductItem();
//            newItem.setSerialNo(UUID.randomUUID().toString()); // gen serialNo unique
//            newItem.setModelId(modelId);
//            newItem.setOrderId(orderId);
//            productItemDAO.save(newItem);
//        }
//    }

    public void adjustProductItemsForOrder(int modelId, long orderId, int quantity) {
        // 1. ดึงรายการ ProductItem ปัจจุบันใน order
        List<ProductItem> currentItems = productItemDAO.findByOrderIdAndModelId(orderId, modelId);
        int currentCount = currentItems.size();

        System.out.println(currentItems);

        // 2. ถ้าจำนวนมากเกินไป → ต้อง "ถอดออก"
        if (currentCount > quantity) {
            int toRemove = currentCount - quantity;
            for (int i = 0; i < toRemove; i++) {
                ProductItem item = currentItems.get(i);
                productItemDAO.clearOrderId(item.getSerialNo()); // set order_id = null
            }
            System.out.println("Removed " + toRemove + " items from order_id=" + orderId);
        }

        // 3. ถ้าจำนวนน้อยเกินไป → ต้อง "เพิ่ม"
        else if (currentCount < quantity) {
            int toAdd = quantity - currentCount;
            // ดึง ProductItem ที่ยังไม่ถูก assign
            List<ProductItem> availableItems = productItemDAO.findAvailableByModelId(modelId);
            int assigned = 0;

            for (ProductItem item : availableItems) {
                if (assigned >= toAdd) break;
                productItemDAO.updateOrder(item.getSerialNo(), orderId);
                assigned++;
            }

            int remaining = toAdd - assigned;

            // ถ้ายังไม่พอ → สร้างใหม่
            for (int i = 0; i < remaining; i++) {
                ProductItem newItem = new ProductItem();
                newItem.setSerialNo(UUID.randomUUID().toString());
                newItem.setModelId(modelId);
                newItem.setOrderId(orderId);
                productItemDAO.save(newItem);
            }

            System.out.println("Added " + toAdd + " items to order_id=" + orderId);
        }

        // 4. ถ้าพอดี → ไม่ต้องทำอะไร
        else {
            System.out.println("Order_id=" + orderId + " already has the correct number of ProductItems.");
        }
    }

    public void addProductItem(int modelId) {
        ProductItem newItem = new ProductItem();
        newItem.setSerialNo(UUID.randomUUID().toString()); // gen serialNo unique
        newItem.setModelId(modelId);
        newItem.setOrderId(null);
        productItemDAO.save(newItem);
    }

//    public void addProductsToWarehouse(int modelId, int qnt) {
//        for (int i=0; i<qnt; i++) {
//            ProductItem newItem = new ProductItem();
//            newItem.setSerialNo(UUID.randomUUID().toString()); // gen serialNo unique
//            newItem.setModelId(modelId);
//            newItem.setOrderId(null);
//            productItemDAO.save(newItem);
//        }
//    }
}