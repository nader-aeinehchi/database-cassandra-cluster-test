package org.naderica.experimental.database.cassandra.cluster.test.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import com.datastax.driver.core.utils.Bytes;

/**
 * Copied from
 * http://stackoverflow.com/questions/25197685/how-can-i-store-objects-in-cassandra-using-the-blob-datatype
 * 
 * <p>All credit to the author.
 *
 */
public interface Bufferable extends Serializable {
    default ByteBuffer serialize() {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytes);) {
            oos.writeObject(this);
            String hexString = Bytes.toHexString(bytes.toByteArray());
            return Bytes.fromHexString(hexString);
        } catch (IOException e) {
            return null;
        }
    }

    public static Bufferable deserialize(ByteBuffer bytes) {
        String hx = Bytes.toHexString(bytes);
        ByteBuffer ex = Bytes.fromHexString(hx);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(ex.array()));) {
            return (Bufferable) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}