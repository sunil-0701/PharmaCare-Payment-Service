package com.pharmacare.report.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "billing", url = "${billing.service.url}")
public interface BillingFeignClient {

	@GetMapping("/api/billing/bills/paid-by-date")
	List<Map<String, Object>> getPaidBillsBetween(
	        @RequestParam("from") String from,
	        @RequestParam("to") String to
	);

    @GetMapping("/api/billing")
    List<Map<String, Object>> getAllBills();
}