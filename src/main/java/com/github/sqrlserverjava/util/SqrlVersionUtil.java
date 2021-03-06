package com.github.sqrlserverjava.util;

import static com.github.sqrlserverjava.backchannel.LoggingUtil.formatForLogging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqrlVersionUtil {
	private static final Logger logger = LoggerFactory.getLogger(SqrlVersionUtil.class);

	private SqrlVersionUtil() {
		// util
	}

	public static Collection<Integer> parseClientVersionString(final String versionStringToParse) {
		// Order of the client set doesn't matter, just parse as the order they were passed
		final List<Integer> clientVersionList = new ArrayList<>();
		// Client can pass version string as 1[,n],[n-m]
		for(String aVersionString : versionStringToParse.split(",")) {
			aVersionString = aVersionString.trim();
			if(aVersionString.contains("-")) {
				// We have a range, extrapolate it
				clientVersionList.addAll(parseVersionRange(aVersionString));
			} else { // Parse as a single version #
				parseToInteger(aVersionString).ifPresent((i) -> clientVersionList.add(i));
			}
		}
		return clientVersionList;
	}

	private static Optional<Integer> parseToInteger(final String intString) {
		try {
			return Optional.of(Integer.parseInt(intString));
		} catch (final NumberFormatException  e) {
			logger.error(formatForLogging("Version parsing failed for '", intString, "'"));
			return Optional.empty();
		}
	}

	/**
	 * Parses a version range string such as "1-3" to a {@code Collection} of {1, 2, 3}
	 *
	 * @param versionRangeString
	 *            a suspected version range string
	 * @return all values that make up the range or an empty {@code Collection} if a parsing error occurred
	 */
	private static Collection<Integer> parseVersionRange(final String versionRangeString) {
		final List<Integer> dataList = new ArrayList<>();
		final String[] partArray = versionRangeString.split("-");
		if (partArray.length != 2) {
			logger.error(formatForLogging("Version range parsing failed for '{}'", versionRangeString));
		} else {
			final Optional<Integer> min = parseToInteger(partArray[0]);
			final Optional<Integer> max = parseToInteger(partArray[1]);
			if (!min.isPresent() || !max.isPresent()) {
				logger.error(formatForLogging("Version range parsing failed for '", versionRangeString, "'"));
				return dataList; // dataList is empty
			}
			final int compared = min.get().compareTo(max.get());
			if (compared == 0) {
				dataList.add(min.get());
			} else if (compared > 0) {
				logger.error(formatForLogging("Version range parsing failed for '", versionRangeString, "'"));
			} else {
				for (int i = min.get(); i < max.get() + 1; i++) {
					dataList.add(i);
				}
			}
		}
		return dataList;
	}

	/**
	 * Compares 2 sets of descending order sorted {@code Integer} sets and finds the highest common version number
	 *
	 * @param serverVersionSet
	 *            a descending order set of SQRL versions that the server supports
	 * @param clientVersionSet
	 *            a collection (ordered or unordered) of SQRL versions that the client supports
	 * @return the highest version in common, or null if none are in common
	 */
	public static Integer findHighestCommonVersion(final Set<Integer> serverVersionSet,
			final Collection<Integer> clientVersionSet) {
		for (final Integer aServerVersion : serverVersionSet) {
			for (final Integer aClientVersion : clientVersionSet) {
				if (aServerVersion.equals(aClientVersion)) {
					return aServerVersion;
				}
			}
		}
		return null;
	}
}
