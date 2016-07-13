package com.github.dbadia.sqrl.server.data;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dbadia.sqrl.server.SqrlFlag;
import com.github.dbadia.sqrl.server.SqrlPersistence;

/**
 * Decorator pattern wrapper for {@link SqrlPersistence} which, when auto-closed, will call
 * {@link SqrlPersistence#closeRollback()} <b>unless</b> a close method was already called; Users <b>must still call
 * {@link SqrlPersistence#closeCommit()} during the happy path</b> as the java try-with-resources semantics gives us no
 * way of knowing if an exception was thrown or not
 * <p>
 * While it is an unfortunate tradeoff to have deviate from the standard try-with-resources pattern, this is the only
 * way we can ensure rollback gets called when both checked and unchecked exceptions occur
 * 
 * @author Dave Badia
 *
 */
public class SqrlAutoCloseablePersistence implements SqrlPersistence, AutoCloseable {
	private static final Logger logger = LoggerFactory.getLogger(SqrlAutoCloseablePersistence.class);
	private final SqrlPersistence sqrlPersistence;

	public SqrlAutoCloseablePersistence(final SqrlPersistence sqrlPersistence) {
		this.sqrlPersistence = sqrlPersistence;
	}

	@Override
	public void close() {
		if (!sqrlPersistence.isClosed()) {
			// Assume an error occurred since SqrlPersistence.commit was not called
			logger.warn("Closing entity manager with rollback in error state");
			sqrlPersistence.closeRollback();
		}
	}

	/* ************** SqrlPersistence delegate methods generated by eclipse ***************/
	@Override
	public void createAndEnableSqrlIdentity(final String sqrlIdk, final Map<String, String> identityDataTable) {
		sqrlPersistence.createAndEnableSqrlIdentity(sqrlIdk, identityDataTable);
	}

	@Override
	public boolean doesSqrlIdentityExistByIdk(final String sqrlIdk) {
		return sqrlPersistence.doesSqrlIdentityExistByIdk(sqrlIdk);
	}

	@Override
	public SqrlIdentity fetchSqrlIdentityByUserXref(final String appUserXref) {
		return sqrlPersistence.fetchSqrlIdentityByUserXref(appUserXref);
	}

	@Override
	public void updateIdkForSqrlIdentity(final String previousSqrlIdk, final String newSqrlIdk) {
		sqrlPersistence.updateIdkForSqrlIdentity(previousSqrlIdk, newSqrlIdk);
	}

	@Override
	public void deleteSqrlIdentity(final String sqrlIdk) {
		sqrlPersistence.deleteSqrlIdentity(sqrlIdk);
	}

	@Override
	public void updateNativeUserXref(final long sqrlIdentityId, final String nativeUserXref) {
		sqrlPersistence.updateNativeUserXref(sqrlIdentityId, nativeUserXref);
	}

	@Override
	public void userAuthenticatedViaSqrl(final String sqrlIdk, final String correlator) {
		sqrlPersistence.userAuthenticatedViaSqrl(sqrlIdk, correlator);
	}

	@Override
	public Boolean fetchSqrlFlagForIdentity(final String sqrlIdk, final SqrlFlag flagToFetch) {
		return sqrlPersistence.fetchSqrlFlagForIdentity(sqrlIdk, flagToFetch);
	}

	@Override
	public void setSqrlFlagForIdentity(final String sqrlIdk, final SqrlFlag flagToSet, final boolean valueToSet) {
		sqrlPersistence.setSqrlFlagForIdentity(sqrlIdk, flagToSet, valueToSet);
	}

	@Override
	public void storeSqrlDataForSqrlIdentity(final String sqrlIdk, final Map<String, String> dataToStore) {
		sqrlPersistence.storeSqrlDataForSqrlIdentity(sqrlIdk, dataToStore);
	}

	@Override
	public String fetchSqrlIdentityDataItem(final String sqrlIdk, final String toFetch) {
		return sqrlPersistence.fetchSqrlIdentityDataItem(sqrlIdk, toFetch);
	}

	@Override
	public boolean hasTokenBeenUsed(final String nutTokenString) {
		return sqrlPersistence.hasTokenBeenUsed(nutTokenString);
	}

	@Override
	public void markTokenAsUsed(final String correlatorString, final String nutTokenString, final Date expiryTime) {
		sqrlPersistence.markTokenAsUsed(correlatorString, nutTokenString, expiryTime);
	}

	@Override
	public String fetchTransientAuthData(final String correlator, final String transientNameServerParrot) {
		return sqrlPersistence.fetchTransientAuthData(correlator, transientNameServerParrot);
	}

	@Override
	public SqrlCorrelator createCorrelator(final String correlatorString, final Date expiryTime) {
		return sqrlPersistence.createCorrelator(correlatorString, expiryTime);
	}

	@Override
	public SqrlCorrelator fetchSqrlCorrelatorRequired(final String correlator) {
		return sqrlPersistence.fetchSqrlCorrelatorRequired(correlator);
	}

	@Override
	public void closeCommit() {
		sqrlPersistence.closeCommit();
	}

	@Override
	public void closeRollback() {
		sqrlPersistence.closeRollback();
	}

	@Override
	public boolean isClosed() {
		return sqrlPersistence.isClosed();
	}

	@Override
	public void cleanUpExpiredEntries() {
		sqrlPersistence.cleanUpExpiredEntries();
	}
}
