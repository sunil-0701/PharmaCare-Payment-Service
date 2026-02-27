package com.pharmacare.report.dto;

import lombok.Data;

@Data
public class ExpiryReportDto {
    private int nearExpiryCount;

	public int getNearExpiryCount() {
		return nearExpiryCount;
	}

	public void setNearExpiryCount(int nearExpiryCount) {
		this.nearExpiryCount = nearExpiryCount;
	}
}