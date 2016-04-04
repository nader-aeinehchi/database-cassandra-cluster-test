package org.naderica.experimental.database.cassandra.cluster.test.database;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.naderica.experimental.database.cassandra.cluster.test.config.Configuration;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class DatabaseManager {

	public final static DatabaseManager INSTANCE = new DatabaseManager();
	public final static String CLUSTER_NAME = "MyCluster";

	private final ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<Integer, Session>();
	private final ConcurrentHashMap<Integer, PreparedStatement> preparedStatements = new ConcurrentHashMap<Integer, PreparedStatement>();

	private DatabaseManager() {
	}

	private Session createSession(final Integer nodeNumber) {
		System.out.println("Creating session for nodeNumber=" + nodeNumber);
		final String hostname = Configuration.SINGLETON.getHost(nodeNumber);
		System.out.println(hostname);
		Cluster cluster = Cluster.builder().addContactPoint(hostname).build();
		return cluster.connect(CLUSTER_NAME);
	}

	private Session getSession(final Integer nodeNumber) {
		return sessions.computeIfAbsent(nodeNumber, (xNode) -> createSession(xNode));
	}

	private PreparedStatement createPreparedStatement(final Integer nodeNumber) {
		System.out.println("createPreparedStatement for nodeNumber=" + nodeNumber);
		final Session session = getSession(nodeNumber);

		final PreparedStatement preparedStatement = session.prepare(
				"INSERT INTO person (pid, posttime, firstname, lastname, address, node, payload) VALUES(?,?,?,?,?,?,?)");

		return preparedStatement;
	}

	private PreparedStatement getPreparedStatement(final Integer nodeNumber) {
		return preparedStatements.computeIfAbsent(nodeNumber, (xNode) -> createPreparedStatement(xNode));
	}

	public void insert(final Integer nodeNumber, final Integer pid, final Payload payload) {
		System.out.println("Insert, nodeNumber=" + nodeNumber + " pid=" + pid);
		final Session session = getSession(nodeNumber);

		final PreparedStatement preparedStatement = getPreparedStatement(nodeNumber);

		final String firstname = "firstname" + pid;
		final String lastname = "lastname" + pid;
		final String address = "address" + pid;
		BoundStatement bound = preparedStatement.bind(pid, new Date(), firstname, lastname, address, nodeNumber,
				payload.serialize());
		session.execute(bound);
	}

	public void selectAll(final Integer nodeNumber) {
		final Session session = getSession(nodeNumber);
		ResultSet resultSet = session.execute("SELECT * FROM person");
		for (Row row : resultSet) {
			System.out.println(row);
		}

	}

}
