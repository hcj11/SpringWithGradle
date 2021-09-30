package shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.startup.UserConfig;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.function.Consumer;

interface Do{
   public String doThis();
}

public class UseController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserConfig.class);

    @Slf4j
    public enum Lockable{

        OptimisicLock((String s)->{
          log.info("OptimisicLock: {}",s);
        }),
        PessimisticLock((String s)->{
            log.info("PessimisticLock : {}",s);
        });
        Consumer<String> consumer;
        Lockable(Consumer<String> consumer) {
            this.consumer=consumer;
        }

        public static void main(String[] args) {
            OptimisicLock.consumer.accept("1");
            OptimisicLock.consumer.accept("2");
        }
    }

//    @RequiresAuthentication()
//    @RequiresRoles(value = "admin")
//    @RequiresPermissions(value = "sys:role:save")
//    @RequiresGuest
//    public ResponseEntity<String> save(){
//        return ResponseEntity.of(Optional.of("hello"));
//    }


}
