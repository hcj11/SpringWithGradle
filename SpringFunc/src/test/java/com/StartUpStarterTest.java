package com;

import context.CompontScan;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
@Configuration
class Dump{
}

@Slf4j
@SpringBootTest(classes = Dump.class)
public class StartUpStarterTest {

    @Autowired
    ApplicationContext applicationContext;



    @Test
    public void startUp() {
        log.info("=================startup");
        CompontScan.print(applicationContext);


    }
}
