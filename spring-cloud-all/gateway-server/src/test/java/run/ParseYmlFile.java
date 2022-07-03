package run;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class ParseYmlFile {


    @Test
    public void parseFile(){
        String content= FileUtil.readString("classpath:application.yml", StandardCharsets.UTF_8);
        Yaml yaml = new Yaml();
        Map<String, Object> oldMap   = yaml.load(content);
        log.info("map:{}",oldMap);
    }
}
