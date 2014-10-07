package org.axway.grapes.core.config;

/**
 * Database configuration
 *
 * <p>Handles the database configuration of Grapes server. The Grapes server will use the targeted
 * database to store the dependencies/licenses information</p>
 *
 * @author jdcoffre
 */
public class DataBaseConfig {

    private String host;

    private int port;

    private String user;

    private String pwd;

    private String datastore;

    private String dbsystem;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public char[] getPwd() {
		return pwd.toCharArray();
	}

	public String getDatastore() {
		return datastore;
	}	

    public String getDbsystem() {
        return dbsystem;
    }    
}
