package com.pharmacare.report.service.impl;

import com.pharmacare.report.client.BillingFeignClient;
import com.pharmacare.report.client.InventoryFeignClient;
import com.pharmacare.report.dto.ReportRequest;
import com.pharmacare.report.service.ReportService;
import com.pharmacare.report.util.PdfGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final BillingFeignClient billingClient;
    private final InventoryFeignClient inventoryClient;
    private final PdfGenerator pdfGenerator;

    public ReportServiceImpl(BillingFeignClient billingClient,
                             InventoryFeignClient inventoryClient,
                             PdfGenerator pdfGenerator) {
        this.billingClient = billingClient;
        this.inventoryClient = inventoryClient;
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public byte[] generatePdf(ReportRequest request) {
        logger.info("Generating report of type: {} from: {} to: {}", 
                request.getReportType(), request.getFromDate(), request.getToDate());
        
        try {
            String type = request.getReportType();

            if ("SALES".equalsIgnoreCase(type)) {
                return generateSalesReport(request);
            }

            if ("PROFIT".equalsIgnoreCase(type)) {
                return generateProfitReport(request);
            }

            if ("EXPIRY".equalsIgnoreCase(type)) {
                return generateExpiryReport();
            }

            throw new RuntimeException("Invalid report type: " + type);
        } catch (Exception e) {
            logger.error("Error during report generation: ", e);
            throw new RuntimeException("Error generating report: " + e.getMessage(), e);
        }
    }

    // ================= SALES =================

    private byte[] generateSalesReport(ReportRequest request) {
        logger.debug("Fetching bills for sales report...");
        List<Map<String, Object>> bills =
                billingClient.getPaidBillsBetween(
                        request.getFromDate().toString(),
                        request.getToDate().toString()
                );

        logger.debug("Received {} bills from billing service", bills != null ? bills.size() : 0);
        
        double revenue = calculateRevenue(bills);
        int transactions = bills != null ? bills.size() : 0;
        double avg = transactions == 0 ? 0 : revenue / transactions;

        logger.debug("Calculated stats - Revenue: {}, Transactions: {}, Average: {}", revenue, transactions, avg);

        return pdfGenerator.generateSalesPdf(
                revenue,
                transactions,
                avg,
                request.getFromDate().toString(),
                request.getToDate().toString()
        );
    }

    // ================= PROFIT =================

    private byte[] generateProfitReport(ReportRequest request) {
        logger.debug("Fetching bills for profit report...");
        List<Map<String, Object>> bills =
                billingClient.getPaidBillsBetween(
                        request.getFromDate().toString(),
                        request.getToDate().toString()
                );

        double revenue = calculateRevenue(bills);
        double profit = revenue * 0.2; // Replace later with real margin logic

        logger.debug("Calculated stats - Revenue: {}, Profit: {}", revenue, profit);

        return pdfGenerator.generateProfitPdf(profit);
    }

    // ================= EXPIRY =================

    private byte[] generateExpiryReport() {
        logger.debug("Fetching medicines for expiry report...");
        List<Map<String, Object>> meds = inventoryClient.getAllMedicines();
        logger.debug("Received {} medicines from inventory service", meds != null ? meds.size() : 0);

        int nearExpiry = 0;

        if (meds != null) {
            for (Map<String, Object> med : meds) {
                Object status = med.get("status");
                if (status != null && status.toString().toUpperCase().contains("NEAR")) {
                    nearExpiry++;
                }
            }
        }

        logger.debug("Identified {} near-expiry medicines", nearExpiry);

        return pdfGenerator.generateExpiryPdf(nearExpiry);
    }

    // ================= COMMON REVENUE CALC =================

    private double calculateRevenue(List<Map<String, Object>> bills) {
        double revenue = 0;

        if (bills == null) return 0.0;

        for (Map<String, Object> bill : bills) {
            Object amt = bill.get("finalAmount");
            if (amt != null) {
                try {
                    String amtStr = amt.toString();
                    revenue += Double.parseDouble(amtStr);
                } catch (NumberFormatException e) {
                    logger.warn("Failed to parse amount: {} for bill: {}", amt, bill.get("id"));
                }
            }
        }

        return revenue;
    }
}
