package org.naderica.experimental.database.cassandra.cluster.test.config;

import java.lang.instrument.Instrumentation;

/**
 * This code has been copied from:
 * https://gist.github.com/EmmanuelOga/404e8d52798473149aaf
 * 
 * <p>
 * All credit to the author.
 *
 */
public class ObjectSizeFetcher {
	private static Instrumentation instrumentation;

	public static void premain(String args, Instrumentation inst) {
		instrumentation = inst;
	}

	public static long getObjectSize(Object o) {
		return instrumentation.getObjectSize(o);
	}
}