import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;

@Plugin(name = "Statistics", category = "Core", elementType = "appender", printObject = true)
public class StatisticsAppender extends AbstractAppender {
    private String url;
    private String driver;
    private String username;
    private String password;
    private static final String sql = "insert into log_records(level, message, timestamp) values(?,?,?)";

    protected StatisticsAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                                 String url,
                                 String driver,
                                 String username,
                                 String password) {
        super(name, filter, layout);
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
    }


    @Override
    public void append(LogEvent logEvent) {
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            Class.forName(this.driver);
            conn = DriverManager.getConnection(url, username, password);
            pst = conn.prepareStatement(sql);
            String level = logEvent.getLevel().toString();
            String msg = logEvent.getMessage().getFormattedMessage();
            Timestamp t = new Timestamp(logEvent.getTimeMillis());
            pst.setString(1, level);
            pst.setString(2, msg);
            pst.setTimestamp(3, t);
            pst.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Something went wrong when insert data into the database!");
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.error("Fail to close connection!");
            }
        }
    }

    @PluginFactory
    public static StatisticsAppender createAppender(@PluginAttribute("name") String name,
                                                    @PluginElement("Filter") final Filter filter,
                                                    @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                    @PluginAttribute("url") String url,
                                                    @PluginAttribute("driver") String driver,
                                                    @PluginAttribute("username") String username,
                                                    @PluginAttribute("password") String password) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (driver == null) {
            LOGGER.error("No driver was found");
            return null;
        }
        if (url == null) {
            LOGGER.error("Please specify database URL!");
            return null;
        }
        if (username == null) {
            LOGGER.error("No username was found when connecting to database");
            return null;
        }
        if (password == null) {
            LOGGER.error("No password was found when connecting to database");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new StatisticsAppender(name, filter, layout, url, driver, username, password);
    }
}
