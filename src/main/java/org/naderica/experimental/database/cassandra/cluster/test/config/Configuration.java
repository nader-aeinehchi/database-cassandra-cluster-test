package org.naderica.experimental.database.cassandra.cluster.test.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.naderica.experimental.database.cassandra.cluster.test.database.Payload;

public class Configuration {

	public final static Configuration SINGLETON = new Configuration();

	final Properties prop = new Properties();

	final Payload payload;

	final Map<Integer, String> hosts = new ConcurrentHashMap<>();

	private Configuration() {
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));

			final int payloadSize = Integer.parseInt(prop.getProperty("payload.size"));
			this.payload = new Payload(payloadSize);
			String[] hostNames = prop.get("hosts").toString().split(",");
			for (int i = 0; i < hostNames.length; i++) {
				hosts.put(i + 1, hostNames[i]);
			}

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public String getClusterName() {
		return prop.getProperty("cluster.name");
	}

	public Integer getClusterSize() {
		// return Integer.parseInt(prop.getProperty("cluster.size"));
		return this.hosts.keySet().size();
	}

	public Integer getNumberOfRowsToInsert() {
		return Integer.parseInt(prop.getProperty("number.of.rows.to.insert"));
	}

	public Integer getNumberOfThreads() {
		return Integer.parseInt(prop.getProperty("number.of.threads"));
	}

	public Payload getPayload() {
		return payload;
	}

	public String getHost(Integer nodeNumber) {
		return hosts.get(nodeNumber);
	}
}
