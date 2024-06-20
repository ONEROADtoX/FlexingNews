package com.oneroad.test;

import com.oneroad.initializer.CacheInitializer;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
public class LogTest {
    @Test
    public void SLF4JTest(){
        Logger logger = LoggerFactory.getLogger(CacheInitializer.class);
        logger.info("This is CacheInitializer.");
    }
}
