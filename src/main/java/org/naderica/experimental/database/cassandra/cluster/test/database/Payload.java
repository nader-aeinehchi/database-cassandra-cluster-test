package org.naderica.experimental.database.cassandra.cluster.test.database;

import java.util.UUID;

public final class Payload implements Bufferable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6106898185128825885L;
	final UUID[] uuids;

	public Payload(int size) {
		final UUID uuid = UUID.randomUUID();
		this.uuids = new UUID[size];
		for (int i = 0; i < size; i++) {
			uuids[i] = uuid;
		}
	}
}