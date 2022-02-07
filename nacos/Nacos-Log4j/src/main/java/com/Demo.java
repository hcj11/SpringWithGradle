package com;

import org.apache.log4j.Logger;

public class Demo {
    Logger logger = Logger.getLogger(Demo.class);
    public void get(){
      logger.info("get something...");
    }

    public void error() {
        logger.error("terrible something...");
    }
}
