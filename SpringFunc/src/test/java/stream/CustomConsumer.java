package stream;

import lombok.extern.slf4j.Slf4j;
import sample.mybatis.domain.User;

import java.util.Map;
import java.util.function.Consumer;
@Slf4j
public class CustomConsumer{

    public void accept(Map.Entry<String, String> map) {
        log.info("{}",map);
    }
}
