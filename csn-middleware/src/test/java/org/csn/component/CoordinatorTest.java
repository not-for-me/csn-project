package org.csn.component;

import static org.junit.Assert.*;

import org.csn.data.CSNConfiguration;
import org.junit.Test;

public class CoordinatorTest {
	private static final String CSN_NAME = "dev-csn";
	private static final String ADMIN_NAME = "NFM";
	private static final String AMDIN_EMAIL = "jwj0831@notforme.kr";
	private static final String NO_STR = "";

	@Test
	public void 이름_매개변수_생성자_테스트() {
		Coordinator coordinator = new Coordinator(CSN_NAME);
		final CSNConfiguration csnConf = coordinator.getCsnConfiguration();
		assertEquals(CSN_NAME, csnConf.getCsnName());
		assertEquals(NO_STR, csnConf.getAdminName());
		assertEquals(NO_STR, csnConf.getAdminEmail());
		assertNotNull(csnConf.getCreationTime());
	}

	@Test
	public void csnConf_매개변수_생성자_테스트() {
		final CSNConfiguration csnConf = new CSNConfiguration(CSN_NAME,
				ADMIN_NAME, AMDIN_EMAIL);
		Coordinator coordinator = new Coordinator(csnConf);
		assertEquals(csnConf, coordinator.getCsnConfiguration());
		assertNotNull(csnConf.getCreationTime());
	}

}
