package com.github.sqrlserverjava.exception;

/**
 * Indicates that data received is invalid
 * 
 * @author Dave Badia
 *
 */
public class SqrlInvalidDataException extends SqrlException {
	private static final long serialVersionUID = 7999271266503909263L;

	/**
	 * {@inheritDoc}
	 */
	public SqrlInvalidDataException(final Throwable e, final CharSequence... messageStringPartArray) {
		super(e, messageStringPartArray);
	}

	/**
	 * {@inheritDoc}
	 */
	public SqrlInvalidDataException(final CharSequence... messageStringPartArray) {
		this(null, messageStringPartArray);
	}
}
