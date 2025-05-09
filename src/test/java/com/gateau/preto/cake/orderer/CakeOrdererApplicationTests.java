package com.gateau.preto.cake.orderer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Test startup application")
class CakeOrdererApplicationTests {

	@Test
	@DisplayName("Test - startup")
	void main() {
		CakeOrdererApplication.main(new String[] {});
	}

}
