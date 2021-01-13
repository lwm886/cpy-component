package com.cpt.tenant;

import com.cpt.tenant.core.TenantScan;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@TenantScan
@EnableApolloConfig
@SpringBootApplication
public class CptTenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(CptTenantApplication.class, args);
	}

}
