package demo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;

public final class LogLevelTestUtil {

    public static void setOff() {
        setOff(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setOff(String name) {
        setLevel(name, Level.OFF);
    }

    public static void setError() {
        setError(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setError(String name) {
        setLevel(name, Level.ERROR);
    }

    public static void setWarn() {
        setWarn(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setWarn(String name) {
        setLevel(name, Level.WARN);
    }

    public static void setInfo() {
        setInfo(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setInfo(String name) {
        setLevel(name, Level.INFO);
    }

    public static void setDebug() {
        setDebug(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setDebug(String name) {
        setLevel(name, Level.DEBUG);
    }

    public static void setTrace() {
        setTrace(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setTrace(String name) {
        setLevel(name, Level.TRACE);
    }

    public static void setAll() {
        setAll(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    public static void setAll(String name) {
        setLevel(name, Level.ALL);
    }

    public static void setRootLevel(Level level) {
        setLevel(org.slf4j.Logger.ROOT_LOGGER_NAME, level);
    }

    public static void setLevel(String loggerName, Level level) {
        final Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        if (logger != null) {
            logger.setLevel(level);
        }
    }

    private LogLevelTestUtil() {
    }
}