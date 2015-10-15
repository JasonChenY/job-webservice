package com.tiaonaer.ws.config;

/**
 * Created by echyong on 10/14/15.
 * to solve the memory leakage problem, where container failed to reregister driver
 * Another alternative is to add following in pom.xml, then mvn wont package this driver to war
 * and put a copy in /usr/share/tomcat7/lib manully.
 <dependency>
 <groupId>mysql</groupId>
 <artifactId>mysql-connector-java</artifactId>
 <version>5.1.18</version>
 <scope>provided</scope>
 </dependency>
 */
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExtendedBasicDataSource extends org.apache.commons.dbcp.BasicDataSource {
    @Override
    public synchronized void close() throws SQLException {
        DriverManager.deregisterDriver(DriverManager.getDriver(url));
        super.close();
    }
}
