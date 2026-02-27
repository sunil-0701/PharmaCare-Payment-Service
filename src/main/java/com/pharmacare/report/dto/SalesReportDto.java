package com.pharmacare.report.dto;

import lombok.Data;

@Data
public class SalesReportDto {
    public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public int getTotalTransactions() {
		return totalTransactions;
	}
	public void setTotalTransactions(int totalTransactions) {
		this.totalTransactions = totalTransactions;
	}
	public double getAverageTicket() {
		return averageTicket;
	}
	public void setAverageTicket(double averageTicket) {
		this.averageTicket = averageTicket;
	}
	private double totalRevenue;
    private int totalTransactions;
    private double averageTicket;
}