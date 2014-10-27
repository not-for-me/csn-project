package org.csn.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    public Connection makeConnection() throws ClassNotFoundException, SQLException, IOException;
}
