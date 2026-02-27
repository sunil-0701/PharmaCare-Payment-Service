package com.pharmacare.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
@EnableFeignClients(basePackages = "com.pharmacare.report.client")
@SpringBootApplication
public class PharmaReportServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmaReportServiceApplication.class, args);
	}

}
