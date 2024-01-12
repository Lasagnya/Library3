package com.project.library3.util;

import org.springframework.web.client.RestClient;

@FunctionalInterface
public interface BankingConnection {
	RestClient.RequestBodySpec getConnection();
}
