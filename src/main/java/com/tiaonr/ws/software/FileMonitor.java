package com.tiaonr.ws.software;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileMonitor.class);
    private static FileAlterationMonitor monitor = null;

    public static void stop() {
        try {
            if ( monitor != null ) monitor.stop();
        } catch ( Exception e ) {
            LOGGER.warn("Failed to stop file monitor", e);
        }
    }
    public static void start(String path) throws Exception {
        // The monitor will perform polling on the folder every 5 seconds
        final long pollingInterval = 60 * 1000;

        File folder = new File(path);

        if (!folder.exists()) {
            // Test to see if monitored folder exists
            throw new RuntimeException("Directory not found: " + path);
        }

        monitor = new FileAlterationMonitor(pollingInterval);
        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationListener listener = new FileAlterationListenerAdaptor() {
            // Is triggered when a file is created in the monitored folder
            @Override
            public void onFileCreate(File file) {
                try {
                    String filename = file.getCanonicalPath();
                    if ( LOGGER.isDebugEnabled() ) LOGGER.debug("File created: " + file.getCanonicalPath());
                    if ( filename.endsWith("versions.txt") ) {
                        SoftwareVersions.reload(filename);
                    }
                } catch (IOException e) {
                    LOGGER.warn("failed", e);
                }
            }

            @Override
            public void onFileChange(File file) {
                try {
                    String filename = file.getCanonicalPath();
                    if ( LOGGER.isDebugEnabled() ) LOGGER.debug("File changed: " + file.getCanonicalPath());
                    if ( filename.endsWith("versions.txt") ) {
                        SoftwareVersions.reload(filename);
                    }
                } catch (IOException e) {
                    LOGGER.warn("failed", e);
                }
            }
            /* Is triggered when a file is deleted from the monitored folder
            @Override
            public void onFileDelete(File file) {
                try {
                    // "file" is the reference to the removed file
                    LOGGER.debug("File removed: " + file.getCanonicalPath());
                    // "file" does not exists anymore in the location
                    LOGGER.debug("File still exists in location: " + file.exists());
                } catch (IOException e) {
                    LOGGER.debug("failed", e);
                }
            }*/
        };

        observer.addListener(listener);
        monitor.addObserver(observer);
        monitor.start();
    }
}
