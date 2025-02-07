package com.shorturl.service;

import org.springframework.stereotype.Service;

@Service
public class LoggerService {

    /**
     * Generic log method to handle logging messages.
     * 
     * @param msg  The message to be logged.
     * @param type The type of log (currently not used).
     */
    protected void log(String msg, int type) {
        System.out.println(msg); // Print the log message to the console
    }

    /**
     * Logs a debug-level message.
     * 
     * @param msg The debug message to be logged.
     */
    public void debug(String msg) {
        log(msg, 0);
    }

    /**
     * Logs an informational message.
     * 
     * @param msg The information message to be logged.
     */
    public void info(String msg) {
        // Currently not implemented, can be extended for logging info-level messages
    }

    /**
     * Logs an error message.
     * 
     * @param msg The error message to be logged.
     */
    public void error(String msg) {
        // Currently not implemented, can be extended for logging error-level messages
    }

    /**
     * Logs a warning message.
     * 
     * @param msg The warning message to be logged.
     */
    public void warning(String msg) {
        // Currently not implemented, can be extended for logging warning-level messages
    }
}
