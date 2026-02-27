package com.pharmacare.report.service;

import com.pharmacare.report.dto.ReportRequest;

public interface ReportService {

    byte[] generatePdf(ReportRequest request);
}