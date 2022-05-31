import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Level;

public class StatisticsLoggerWrapper {
    private final String LoggerName;
    private Logger logger;


    protected StatisticsLoggerWrapper(String loggerName) {
        LoggerName = loggerName;
    }

    public String getLoggerName()
    {
        return LoggerName;
    }

    public void debug(Object message)
    {
        logger.log(Level.DEBUG, message);
    }

    public void debug(Object message, Throwable t)
    {
        logger.log(Level.DEBUG, message, t);
    }

    public boolean isInfoEnabled()
    {
        Level p = Level.INFO;
        if(!logger.isEnabledFor(p))
            return false;
        else
            return p.isGreaterOrEqual(log.getEffectiveLevel());
    }

    public void info(Object message)
    {
        logger.log(Level.INFO, message);
    }

    public void info(Object message, Throwable t)
    {
        logger.log(Level.INFO, message, t);
    }

    public void warn(Object message)
    {
        logger.log(Level.WARN, message);
    }

    public void warn(Object message, Throwable t)
    {
        logger.log(Level.WARN, message, t);
    }

    public void error(Object message)
    {
        logger.log(Level.ERROR, message);
    }

    public void error(Object message, Throwable t)
    {
        logger.log(Level.ERROR, message, t);
    }

    public void fatal(Object message)
    {
        logger.log(Level.FATAL, message);
    }

    public void fatal(Object message, Throwable t)
    {
        logger.log(Level.FATAL, message, t);
    }

    public void log(Level p, Object message)
    {
        logger.log(p, message);
    }

    public void log(Level p, Object message, Throwable t)
    {
        logger.log(p, message, t);
    }

    public static StatisticsLoggerWrapper getLogger(String name)
    {
        StatisticsLoggerWrapper logger = new StatisticsLoggerWrapper(name);
        return logger;
    }

    public static StatisticsLoggerWrapper getLogger(Class fqcn)
    {
        StatisticsLoggerWrapper logger = new StatisticsLoggerWrapper(fqcn.getName());
        return logger;
    }
}
