package com.github.dbadia.sqrl.server.backchannel;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.dbadia.sqrl.server.backchannel.SqrlTif.TifBuilder;

@RunWith(Parameterized.class)
public class TifTest {
	@Parameters(name = "{index}: url=({0}) escheme=({1}) eurl=({2}) eport=({3}) euri=({4})")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] { 
			{ true, 5, new int[]{SqrlTif.TIF_CURRENT_ID_MATCH }},
			{ true, -60, new int[]{SqrlTif.TIF_COMMAND_FAILED, SqrlTif.TIF_CLIENT_FAILURE} },
		});
	}
	// @formatter:on

	@Test
	public void testIt() throws Exception {
		final TifBuilder builder = new TifBuilder(ipsMatched);
		final List<Integer> absentTifList = SqrlTif.getAllTifs();
		if(ipsMatched) {
			absentTifList.remove(SqrlTif.TIF_IPS_MATCHED);
		}
		for (final int tif : tifList) {
			builder.addFlag(tif);
			absentTifList.remove(Integer.valueOf(tif));
		}

		final SqrlTif tif = builder.createTif();

		if (ipsMatched) {
			assertTrue(isTifPresent(tif, SqrlTif.TIF_IPS_MATCHED));
		}
		for (final int expectedTif : tifList) {
			assertTrue(isTifPresent(tif, expectedTif));
		}
		assertEquals(expectedValue, tif.getTifInt());

		// TODO: test with absent
	}


	/* **************** Util methods *****************/
	static final void assertTif(final SqrlTif tif, final int... expectedTifArray) {
		for (final int expectedTifInt : expectedTifArray) {
			assertTifPresent(tif, expectedTifInt);
		}
		final List<Integer> absentTifList = buildAbsentTifList(expectedTifArray);
		for (final int absentTifInt : absentTifList) {
			assertTifAbsent(tif, absentTifInt);
		}
	}

	private static void assertTifAbsent(final SqrlTif tif, final int absentTifInt) {
		assertTrue("Found expected absent " + absentTifInt + " in tif " + tif, isTifAbsent(tif, absentTifInt));
	}

	static final List<Integer> buildAbsentTifList(final int[] expectedTifArray) {
		final List<Integer> absentTifList = SqrlTif.getAllTifs();
		for (final int tif : expectedTifArray) {
			absentTifList.remove(Integer.valueOf(tif));
		}
		return absentTifList;
	}

	static final void assertTifPresent(final SqrlTif tif, final int tifToLookFor) {
		assertTrue("Expected " + tifToLookFor + " in tif " + tif, isTifPresent(tif, tifToLookFor));
	}

	static final boolean isTifPresent(final SqrlTif tif, final int tifToLookFor) {
		return (tif.getTifInt() & tifToLookFor) == tifToLookFor;
	}

	static final boolean isTifAbsent(final SqrlTif tif, final int tifToLookFor) {
		return !isTifPresent(tif, tifToLookFor);
	}

	// Instance variables and constructor are all boilerplate for Parameterized test, so put them at the bottom
	private final boolean ipsMatched;
	private final int expectedValue;
	private final int[] tifList;

	public TifTest(final boolean ipsMatched, final int expectedValue, final int... tifList) {
		super();
		this.ipsMatched = ipsMatched;
		this.expectedValue = expectedValue;
		this.tifList = tifList;
	}

}