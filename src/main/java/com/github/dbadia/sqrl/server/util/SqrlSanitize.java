package com.github.dbadia.sqrl.server.util;

/**
 * Helper class to ensure data coming from the Internet is sanitized against security vulnerabilities such as XSS. If
 * any issues are found, {@link SqrlIllegalDataException} is thrown to halt the transaction
 *
 * @author Dave Badia
 *
 */
public class SqrlSanitize {

	/**
	 * Performs basic security checks for size and character format for all SQRL tokens: nut, correlator, param, etc
	 *
	 * @param data
	 *            the data to be examined
	 * @throws SqrlIllegalDataException
	 *             if data validation fails
	 */
	public static void inspectIncomingSqrlData(final String data) throws SqrlIllegalDataException {
		if (SqrlUtil.isBlank(data)) {
			return;
		}
		if(data.length() > SqrlConstants.MAX_SQRL_TOKEN_SIZE) {
			throw new SqrlIllegalDataException("Data exceeded max size");
		} else if (!SqrlUtil.REGEX_PATTERN_REGEX_BASE64_URL.matcher(data).matches()) {
			throw new SqrlIllegalDataException("Data failed base64url validation: '" + data + "'");
		}
	}

}