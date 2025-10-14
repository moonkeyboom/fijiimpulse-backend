package sa3.fijiimpulse.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.service.MaterialService;
import sa3.fijiimpulse.service.RecipeMaterialService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;
    private final RecipeMaterialService recipeMaterialService;

    public MaterialController(MaterialService materialService, RecipeMaterialService recipeMaterialService) {
        this.materialService = materialService;
        this.recipeMaterialService = recipeMaterialService;
    }

    // ==================== Use Case 3M: ตรวจสอบทรัพยากร ====================

    @GetMapping("/products")
    public ResponseEntity<List<ProductItem>> getAllProductItems() {
        List<ProductItem> products = materialService.getAllProductItems();
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<Material> getMaterialById(@PathVariable int materialId) {
        Material material = materialService.getMaterialById(materialId);
        return ResponseEntity.ok(material);
    }

    // ==================== Use Case 4M: ได้รับวัสดุอุปกรณ์ ====================

    @PutMapping("/{materialId}/receive")
    public ResponseEntity<String> receiveMaterial(
            @PathVariable int materialId,
            @RequestParam int quantity
    ) {
        boolean success = materialService.receiveMaterial(materialId, quantity);

        if (success) {
            return ResponseEntity.ok("อัปเดตจำนวนวัสดุสำเร็จ");
        } else {
            return ResponseEntity.status(400).body("อัปเดตไม่สำเร็จ (ตรวจสอบ materialId หรือ quantity)");
        }
    }

    @PutMapping("/{materialId}/remove")
    public ResponseEntity<String> removeMaterial(
            @PathVariable int materialId,
            @RequestParam int quantity
    ) {
        boolean success = materialService.reduceMaterial(materialId, quantity);

        if (success) {
            return ResponseEntity.ok("ลดจำนวนวัสดุสำเร็จ");
        } else {
            return ResponseEntity.status(400).body("ลดไม่สำเร็จ (Stock ไม่พอหรือ quantity ไม่ถูกต้อง)");
        }
    }

    @PutMapping("/reduce-from-produce")
    public void reduceMaterialFromProduce(@RequestBody Map<Integer, Integer> body) {
        for (Map.Entry<Integer, Integer> entry : body.entrySet()) {
            int modelId = entry.getKey();
            int quantity = entry.getValue();
            Map<Integer, Map<String, Object>> recipe = recipeMaterialService.getModelRecipe(modelId);
            for (Map.Entry<Integer, Map<String, Object>> modelEntry : recipe.entrySet()) {
                int matId = modelEntry.getKey();
                System.out.println("matid" + matId);
                Object reqObj = modelEntry.getValue().get("required_quantity");
                int requireQnt = ((Number) reqObj).intValue() * quantity;
                System.out.println("req" + requireQnt);
                materialService.reduceMaterial(matId, requireQnt);
            }
        }
    }
}
