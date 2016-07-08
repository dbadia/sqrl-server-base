package com.github.dbadia.sqrl.server.backchannel;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.github.dbadia.sqrl.server.SqrlConfig;
import com.github.dbadia.sqrl.server.SqrlFlag;
import com.github.dbadia.sqrl.server.SqrlPersistence;
import com.github.dbadia.sqrl.server.TCUtil;
import com.github.dbadia.sqrl.server.backchannel.SqrlTif.TifBuilder;

public class SqrlServerOperationsClientOptsTest {
	final String correlator = "abc";
	private SqrlConfig config;
	private SqrlPersistence sqrlPersistence;
	private SqrlServerOperations sqrlServerOps;
	private TifBuilder tifBuilder;
	private SqrlNutToken nutToken;

	@Before
	public void setUp() throws Exception {
		sqrlPersistence = TCUtil.buildEmptySqrlPersistence();
		config = TCUtil.buildValidSqrlConfig();
		config.setNutValidityInSeconds(Integer.MAX_VALUE);
		sqrlServerOps = new SqrlServerOperations(sqrlPersistence, config);
		tifBuilder = new TifBuilder();
		nutToken = TCUtil.buildValidSqrlNut(config, LocalDateTime.now());
	}

	@Test
	public void testOptCps_NotSupported() throws Throwable {
		// Setup
		final String idk = "m470Fb8O3XY8xAqlN2pCL0SokqPYNazwdc5sT6SLnUM";
		sqrlPersistence.createAndEnableSqrlIdentity(idk, Collections.emptyMap());
		sqrlPersistence.setSqrlFlagForIdentity(idk, SqrlFlag.SQRL_AUTH_ENABLED, true);
		final SqrlRequest sqrlRequest = TCBackchennelUtil.buildMockSqrlRequest(idk, "ident", false, SqrlClientOpt.cps);

		// Execute
		final boolean idkExists = sqrlServerOps.processClientCommand(sqrlRequest, nutToken, tifBuilder, correlator);

		// Validate
		assertTrue(idkExists);
		final SqrlTif tif = tifBuilder.createTif();
		SqrlTifTest.assertTif(tif, SqrlTif.TIF_CURRENT_ID_MATCH, SqrlTif.TIF_FUNCTIONS_NOT_SUPPORTED);
		// Ensure nothing should get disabled
		assertTrue(sqrlPersistence.fetchSqrlFlagForIdentity(idk, SqrlFlag.SQRL_AUTH_ENABLED));
		assertTrue(sqrlPersistence.doesSqrlIdentityExistByIdk(idk));
	}
}
