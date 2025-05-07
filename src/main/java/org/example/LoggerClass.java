package org.example;

import java.time.format.DateTimeFormatter;

public class LoggerClass {
    public static final Logger logger;

    static {
        LoggerConfig config = new LoggerConfig.Builder()
                .async(true)
                .bufferSize(25)
                .formatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .addSink(new StdoutSink(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        logger = LoggerDistributor.build(config);
    }
}
