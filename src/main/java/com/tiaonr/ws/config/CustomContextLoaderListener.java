package com.tiaonr.ws.config;

/**
 * Created by echyong on 11/12/15.
 */

import com.tiaonr.ws.software.SoftwareVersions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.ServletContextEvent;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import java.lang.InterruptedException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Driver;
import java.util.List;
import java.util.Collections;
import com.tiaonr.ws.software.FileMonitor;

public class CustomContextLoaderListener extends ContextLoaderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomContextLoaderListener.class);

    public CustomContextLoaderListener(WebApplicationContext context) {
        super(context);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        try {
            FileMonitor.start(event.getServletContext().getRealPath("/static/app"));
            SoftwareVersions.reload(event.getServletContext().getRealPath("/static/app/versions.txt"));
        } catch ( Exception e) {
            LOGGER.warn("failed to start FileMonitor", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        FileMonitor.stop();

        /*
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
        for(Thread t:threadArray) {
            if(t.getName().contains("Abandoned connection cleanup thread")) {
                synchronized(t) {
                    t.stop(); //don't complain, it works
                }
            }
        }
        */
        try {
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
        }

        try {
            List drivers = Collections.list(DriverManager.getDrivers());
            for (int i=0;i<drivers.size();i++){
                Driver driver = (Driver)drivers.get(i);
                //LOGGER.debug("Deregister driver " + driver.getClass().getName());
                DriverManager.deregisterDriver(driver);
            }
        } catch ( SQLException e ) {
            //LOGGER.debug("exception:", e);
        }

        super.contextDestroyed(event);

        /* Deregister driver
        Other Alternatives:
        - add following in pom.xml, then mvn wont package this driver to war and put a copy in /usr/share/tomcat7/lib manully.
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.18</version>
            <scope>provided</scope>
        </dependency>
        - Override close method of com.jolbox.bonecp.BoneCPDataSource, and do the samething here.
        */
    }
}
