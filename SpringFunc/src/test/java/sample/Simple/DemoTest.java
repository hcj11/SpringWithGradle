package sample.Simple;

import cn.hutool.core.io.checksum.CRC16;
import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;
@Slf4j
public class DemoTest {

    /**
     *
     */
    @Test
    public void demo1(){
        String s = UUID.randomUUID().toString();
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes(StandardCharsets.UTF_8));
        long value = crc32.getValue() ;
        // 0-9
        long mod= (long)value % 10;
        log.info("{},{}",value,mod);
    }
}
