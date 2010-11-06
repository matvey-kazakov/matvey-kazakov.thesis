// $Id: MKLogger.java,v 1.1 2006/02/18 07:00:34 matvey Exp $
/**
 * Date: 03.07.2005
 * Copyright (c) Matvey Kazakov 2004.
 */
package matvey.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;

/**
 * @author Matvey Kazakov
 */
public class MKLogger {
    private static Logger rootLogger = Logger.getLogger("ROOT");
    private static Set<String> allocatedLoggers = new HashSet<String>();
    private static MKLogFormatter logFormatter = new MKLogFormatter();
    private static int maxLoggerName;

    static {
        rootLogger.setUseParentHandlers(false);
    }

    public static void logToConsole(Level level) {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(level);
        consoleHandler.setFormatter(logFormatter);
        rootLogger.addHandler(consoleHandler);
    }

    public static void logToFile(Level level, String fileName) {
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(fileName);
        } catch (IOException e) {
            return;
        }
        fileHandler.setLevel(level);
        rootLogger.addHandler(fileHandler);
    }

    /**
     * Creates simple logger that will be a part of root logger.
     * @param name its name
     * @return created logger
     */
    public static synchronized Logger createLogger(String name) {
        return createLogger(name, null, null);
    }

    /**
     * Creates simple logger that will be a part of root logger with level defined.
     * @param name its name
     * @param level its level
     * @return created logger
     */
    public static synchronized Logger createLogger(String name, Level level) {
        return createLogger(name, level, null);
    }

    /**
     * Creates simple logger that will be a part of root logger with level and file defined.
     *
     * @param name its name
     * @param level level for both - logger and file
     * @param fileName name of the file
     * @return created logger
     */
    public static synchronized Logger createLogger(String name, Level level, String fileName) {
        Logger logger = Logger.getLogger(name);
        if (level != null) {
            logger.setLevel(level);
        }
        if (allocatedLoggers.contains(name)) {
            return logger;
        } else {
            allocatedLoggers.add(name);
            logger.setParent(rootLogger);
            if (maxLoggerName <= name.length()) {
                maxLoggerName = name.length();
            }
            if (fileName != null) {
                try {
                    Handler fileHandler = null;
                    fileHandler = new FileHandler(fileName);
                    if (level != null) {
                        fileHandler.setLevel(level);
                    }
                    logger.addHandler(fileHandler);
                } catch (IOException e) {
                    // ust skip - file handler won't be created
                }
            }
            return logger;
        }
    }

    private static class MKLogFormatter extends Formatter {
        Date date = new Date();
        private final static String dateFormat = "{0,date} {0,time} ";
        private MessageFormat dateFormatter = new MessageFormat(dateFormat);

        private Object dateFormatArgs[] = new Object[1];

        // Line separator string.  This is the value of the line.separator
        // property at the moment that the SimpleFormatter was created.
        private String lineSeparator = (String) java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction("line.separator"));

        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            // Minimize memory allocations here.
            date.setTime(record.getMillis());
            dateFormatArgs[0] = date;
            sb.append(dateFormatter.format(dateFormatArgs, new StringBuffer(), null));
            sb.append(record.getLoggerName());
            for (int i = record.getLoggerName().length(); i < maxLoggerName + 1; i++) {
                sb.append(' ');
            }
            String message = formatMessage(record);
            sb.append(level2string(record.getLevel()));
            sb.append(message);
            sb.append(lineSeparator);
            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                }
            }
            return sb.toString();
        }


        private static int[] levelValues = new int[]{
                Level.SEVERE.intValue(),
                Level.WARNING.intValue(),
                Level.INFO.intValue(),
                Level.CONFIG.intValue(),
                Level.FINE.intValue(),
                Level.FINER.intValue(),
                Level.FINEST.intValue()
        };
        private static String[] levelNames = new String[]{
            "SEVERE:  ",
            "WARNING: ",
            "INFO:    ",
            "CONFIG:  ",
            "FINE:    ",
            "FINER:   ",
            "FINEST:  "
        };

        public static String level2string(Level level) {
            for (int i = 0; i < levelValues.length; i++) {
                if (levelValues[i] == level.intValue()) {
                   return levelNames[i];
                };
            }
            return level.getName()+": ";
        }
    }
}

/*
 * $Log: MKLogger.java,v $
 * Revision 1.1  2006/02/18 07:00:34  matvey
 * just added
 *
 */