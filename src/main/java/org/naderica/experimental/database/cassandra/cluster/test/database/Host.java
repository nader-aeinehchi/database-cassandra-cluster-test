package org.naderica.experimental.database.cassandra.cluster.test.database;

public enum Host {

	node1("127.0.0.1"), node2("127.0.0.2"), node3("127.0.0.3"), node4("127.0.0.4"), node5("127.0.0.5");

	final String ip;

	Host(final String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return ip;
	}

	/**
	 * If no valid <code>nodeNumber</code>, returns the first node
	 * 
	 * @param nodeNumber
	 * @return
	 */
	public static Host get(final Integer nodeNumber) {
		switch (nodeNumber) {
		case 1:
			return node1;
		case 2:
			return node2;
		case 3:
			return node3;
		case 4:
			return node4;
		case 5:
			return node5;
		default:
			return node1;
		}
	}

}
