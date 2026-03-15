package com.example.springai.tools;

import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class SpringAiTools {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SpringAiTools.class);

    @Tool(description = "this method provides the current date and time as per user time zone")
    public String getCurrentDateTime() {
        log.info("SpringAiTools.getCurrentDateTime");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
