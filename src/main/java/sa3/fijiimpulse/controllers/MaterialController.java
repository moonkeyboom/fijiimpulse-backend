package sa3.fijiimpulse.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sa3.fijiimpulse.entity.Material;
import sa3.fijiimpulse.entity.ProductItem;
import sa3.fijiimpulse.service.MaterialService;
import sa3.fijiimpulse.service.RecipeMaterialService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/materials")
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

    // ==================== CRUD Operations ====================

    @PostMapping
    public ResponseEntity<String> createMaterial(@RequestBody Material material) {
        try {
            materialService.createMaterial(material);
            return ResponseEntity.ok("Material created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to create material: " + e.getMessage());
        }
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<String> updateMaterial(@PathVariable int materialId, @RequestBody Material material) {
        try {
            material.setMaterialId(materialId);
            materialService.updateMaterial(material);
            return ResponseEntity.ok("Material updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update material: " + e.getMessage());
        }
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<String> deleteMaterial(@PathVariable int materialId) {
        try {
            materialService.deleteMaterial(materialId);
            return ResponseEntity.ok("Material deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete material: " + e.getMessage());
        }
    }

    // ==================== Upload Material with Image ====================

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMaterial(
            @RequestParam int supplierId,
            @RequestParam String materialName,
            @RequestParam int stockQuantity,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            materialService.saveMaterialWithImage(supplierId, materialName, stockQuantity, file);
            return ResponseEntity.ok("Material uploaded successfully with image!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to upload material: " + e.getMessage());
        }
    }

    @PutMapping("/upload/{materialId}")
    public ResponseEntity<String> updateMaterialWithImage(
            @PathVariable int materialId,
            @RequestParam int supplierId,
            @RequestParam String materialName,
            @RequestParam int stockQuantity,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            materialService.updateMaterialWithImage(materialId, supplierId, materialName, stockQuantity, file);
            return ResponseEntity.ok("Material updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to update material: " + e.getMessage());
        }
    }

    // ==================== Image Endpoints ====================
    @GetMapping("/image/{materialId}")
    public ResponseEntity<byte[]> getMaterialImage(@PathVariable int materialId) throws IOException {
        Material material = materialService.getMaterialById(materialId);
        if (material == null || material.getMaterialImagePath() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body("Material image not found".getBytes());
        }

        Path imgPath = Paths.get(System.getProperty("user.dir"), material.getMaterialImagePath());
        if (!Files.exists(imgPath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                    .body("Image file not found".getBytes());
        }

        byte[] imageBytes = Files.readAllBytes(imgPath);
        HttpHeaders headers = new HttpHeaders();
        String ext = material.getMaterialImagePath().substring(material.getMaterialImagePath().lastIndexOf('.') + 1).toLowerCase();
        if ("png".equals(ext)) headers.setContentType(MediaType.IMAGE_PNG);
        else if ("jpg".equals(ext) || "jpeg".equals(ext)) headers.setContentType(MediaType.IMAGE_JPEG);
        else headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
