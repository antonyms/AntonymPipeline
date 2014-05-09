/*******************************************************************************
 * Copyright 2012
 * FG Language Technologie
 * Technische Universitaet Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.jobimtext.api.db;

/** 
 * @author Martin Riedl
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that handles opening and closeing a database connectino
 * @author Martin Riedl (riedl@cs.tu-darmstadt.de)
 *
 */
public class DatabaseConnection {
	private Connection conn;


	public void openConnection(String url, String user, String password,
			String jdbcDriver) throws ClassNotFoundException, SQLException {
		// JDBC driver registration
		Class.forName(jdbcDriver);
		// Connection opening
		System.out.println("[Database] Connecting to a selected database...");
		
		conn = DriverManager.getConnection(url, user, password);
		
		System.out.println("[Database] Connected database successfully...");
	}

	public Connection getConnection() {
		return conn;
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
