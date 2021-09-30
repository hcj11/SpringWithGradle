package Quartz;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RRException extends RuntimeException {
    public RRException(String msg, Exception e) {
      log.error("{},{}",msg,e.getMessage());
    }
}
