package com.pharmacare.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "inventory-service",
        url = "http://localhost:9093"
)
public interface InventoryFeignClient {

    @GetMapping("/api/medicines")
    List<Map<String, Object>> getAllMedicines();
}