package com.pharmacare.report.dto;

import lombok.Data;

@Data
public class ProfitReportDto {
    private double totalProfit;

	public double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(double totalProfit) {
		this.totalProfit = totalProfit;
	}
}