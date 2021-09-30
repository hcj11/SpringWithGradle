package Mvc;

import Mvc.serde.PhoneObjectDeserialize;
import Mvc.serde.PhoneObjectSerializer;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@JsonAutoDetect(setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JSONType(serialzeFeatures = {
        SerializerFeature.WriteClassName
        , SerializerFeature.IgnoreErrorGetter,
        SerializerFeature.WriteNullNumberAsZero
})
@Data
class Param {
    Page page;
    PageInfo pageInfo;
    Date dates;

    String othersStr;

    Integer others;

    @JSONField(serializeUsing = PhoneObjectSerializer.class, deserializeUsing = PhoneObjectDeserialize.class)
    @NotNull
    @Size(max = 13, min = 2, groups = {Default.class}, message = "参数值长度不合法")
    String phone;
    @Builder.Default
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JSONField(format = "yyyy/MM/dd HH:mm:ss")
    LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault()).plus(1, ChronoUnit.DAYS);
    @Builder.Default
    Account account = new Account("1");

    /**
     * static{}  first and maps last
     */
    static {
        log.info("Param clint");
    }

    // static
    final HashMap<String, String> maps = Maps.newHashMap();
    @Builder.Default
    @JSONField(serialzeFeatures = {SerializerFeature.WriteNullListAsEmpty})
    List<String> details = Lists.newArrayList();


    @Data
    public static class Account {
        static {
            log.info("Account clint");
        }

        String accountId;

        public Account(String accountId) {
            this.accountId = accountId;
        }
    }


}

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class MessageDto {
    @NotBlank
    private String msg;
    private String status;
}

@Slf4j
@RestControllerAdvice
class Advice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {org.springframework.web.bind.MethodArgumentNotValidException.class})
    public MessageDto badRequest(org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.error("{}", e.getMessage());
        return MessageDto.builder().msg("参数校验失败").status("0").build();
    }
}

@RequestMapping("/web")
@Slf4j
@RestController
class WebController {

    @GetMapping("/get")
    public MessageDto get() {
        return new MessageDto("yes", "0");
    }
}

@Slf4j
@RestController
public class Controller {


    @GetMapping("deferredResult")
    public DeferredResult deferredResult() {

        DeferredResult deferredResult = new DeferredResult(10L, () -> {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hellow";
        });
        deferredResult.onTimeout(() -> {
            log.info("timeOut");
        });
        deferredResult.onCompletion(() -> {
            log.info("completion");
        });
        deferredResult.onError((Object throwable) -> {
            log.info("error,{}", ((Throwable) throwable).getMessage());
        });
        return deferredResult;
    }

    /**
     *
     */
    @GetMapping("callable")
    public Callable callable() {
        return new Callable() {

            @Override
            public Object call() throws Exception {
                Thread.sleep(10000);
                return "10s end";
            }
        };
    }

    @PostMapping("insert")
    public Param insert(@RequestBody @Validated(value = {ParamCheckSequence.class})
                                Param param) {
        log.info("{}", param);
        return param;
    }

    @PutMapping("update")
    public MessageDto update(@RequestHeader(value = "attr") String attr, @RequestBody HashMap map) {
        log.info("{}", map);
        return new MessageDto();
    }

    @PostMapping("post")
    public MessageDto post(@RequestHeader(value = "attr") String attr, @Valid @RequestBody ObjectJson objectJson, Errors errors) {
        if (errors.hasErrors()) {
            return new MessageDto("nothing", "-1");
        }
        log.info("{}", objectJson);
        return new MessageDto();
    }

    @GetMapping("get")
    public MessageDto get() {
        return new MessageDto("msg: helloworld", "0");
    }


    public static void test1() {
        Page<String> page = new Page<String>();
        page.setTotal(111);
        page.setPageNum(1);
        page.setPageSize(10);
        page.add("1");
        PageInfo<String> stringPageInfo = page.toPageInfo();

        Param param1 = Param.builder().pageInfo(stringPageInfo).build();
        String s = JSONObject.toJSONString(param1);

        log.info("{}", s);
    }

    public static void test2() {
        long now1 = new Date().getTime() / 100;
        long now2 = new Date().getTime() / 100;
        Assert.isTrue(now1 == now2);

        BigDecimal divide = new BigDecimal(3).divide(new BigDecimal(3), 3, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        System.out.println(divide.setScale(1).stripTrailingZeros().toPlainString());
        System.out.println(divide.stripTrailingZeros());

        System.out.println(divide.setScale(1).stripTrailingZeros().toPlainString());
        System.out.println(divide.stripTrailingZeros().toPlainString());

        Param.Account account = new Param.Account(UUID.randomUUID().toString());
        Param build = Param.builder().phone("17317298371")
                .localDateTime(LocalDateTime.now()).account(account).dates(new Date())
                .details(Lists.newArrayList("1","2")).build();
        String s = JSONObject.toJSONString(build);
        log.info("{}", s);
        /**
         * {
         *     "phone":17317298371
         * }
         */
        Param param = JSONObject.parseObject(s, Param.class);
        System.out.println(param);
    }

    public static void main(String[] args) throws InterruptedException {

        test2();


    }
}
