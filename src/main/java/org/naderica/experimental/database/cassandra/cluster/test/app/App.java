package org.naderica.experimental.database.cassandra.cluster.test.app;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.naderica.experimental.database.cassandra.cluster.test.config.Configuration;
import org.naderica.experimental.database.cassandra.cluster.test.database.DatabaseManager;
import org.naderica.experimental.database.cassandra.cluster.test.database.Payload;

/**
 * Hello world!
 *
 */
public class App {
	final static App SINGLETON = new App();
	final String clusterName = Configuration.SINGLETON.getClusterName();
	final Integer clusterSize = Configuration.SINGLETON.getClusterSize();
	final Integer numberOfRowsToInsert = Configuration.SINGLETON.getNumberOfRowsToInsert();

	final Random nodeRandomizer = new Random();

	private Integer getRandomNode() {
		final Integer number = nodeRandomizer.nextInt(clusterSize + 1);
		return (number == 0) ? number + 1 : number;
	}

	public void task(final Integer nodeNumber, final Integer pid, final Payload payload) {
		DatabaseManager.INSTANCE.insert(App.SINGLETON.getRandomNode(), pid, payload);
	}

	public static void main(String[] args) {

		System.out.println("Running on cluster=" + SINGLETON.clusterName);

		final Payload payload = Configuration.SINGLETON.getPayload();

		final Integer numberOfThreads = Configuration.SINGLETON.getNumberOfThreads();

		final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		for (int i = 1; i < SINGLETON.numberOfRowsToInsert + 1; i++) {
			final Integer taskNumber = i;
			executorService.submit(() -> {
				DatabaseManager.INSTANCE.insert(App.SINGLETON.getRandomNode(), taskNumber, payload);
			});
		}

	}

}
