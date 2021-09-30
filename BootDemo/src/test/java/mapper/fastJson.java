package mapper;

import Mvc.serde.StringDeserialize;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


@JSONType
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class AA implements Serializable {


    private String apple;
    private CustomA.CustomB customB;

}


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class CustomA<T> implements Serializable {

    private static final long serialVersionUID = -8915474832215578745L;
    @JSONField(deserializeUsing = StringDeserialize.class)
    private String name;
    private T object;
    private CustomB customB;
    @Builder.Default
    private ArrayList<Integer> integers = Lists.<Integer>newArrayList(1, 2, 3);

    @JSONType
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    static class CustomB implements Serializable {
        private static final long serialVersionUID = -3012130568181607956L;
        private CustomC customC;
        private Integer age;

        @Data
        class CustomC implements Serializable {
            private String info;
        }
    }
}

@Slf4j
public class fastJson {
    @Test
    public void restTemplateTest() {
        RestTemplate restTemplate = new RestTemplate();
        String s = restTemplate.postForObject("http://www.baidu.com", null, String.class);
        log.info("===========1. {}", s);
        HashMap<String, String> stringStringHashMap = Maps.<String, String>newHashMap();
        stringStringHashMap.put("userId", "1");
        String s1 = restTemplate.postForObject("http://www.baidu.com", stringStringHashMap, String.class);
        log.info("===========2. {}", s1);
        /**
         * restTemplate.postForObject();
         */


    }
    /**
     *  
     */
    @Test
    public void try1(){

    }

    @Test
    public void hello() throws JsonProcessingException {
        /**
         * Acc
         */
        CustomA objectCustomA = CustomA.builder().name(null).build();
        log.info("==============={}", objectCustomA);
        String oo = JSON.toJSONString(objectCustomA, true);
        String s = JSONObject.toJSONString(objectCustomA, true);
        log.info("==============={},{}", oo, s);

        // "" -> null
        CustomA.CustomB customB = CustomA.CustomB.builder().age(null).build();
        AA aa = AA.builder().apple("").build();
        CustomA<AA> iphone4 = CustomA.<AA>builder().name("")
                .object(aa).customB(customB).build();

        log.info("====apple====={}", iphone4);

        String o = JSON.toJSONString(iphone4, true);

        log.info("====make string=={}", o);
//        JSONObject.parseObject()
//        new ParameterizedTypeImpl();
//        new ParameterizedType().;
//        JSONObject.parseObject(s, ParameterizedType<AA>.class);

        CustomA<JSONObject> customA1 = JSONObject.parseObject(o, CustomA.class);
        log.info("====customA1====={}", customA1);

        String s2 = customA1.getObject().toString();

        String s1 = JSONObject.toJSONString(customA1.getObject());
        log.info("====customA1 sub elem====={},{}", s1, s2);
        AA aa1 = JSONObject.parseObject(s1, AA.class);
        log.info("====parse customA1 sub elem====={}", aa1);
        /**
         * T -> jsonObject;
         */
        JSONObject object = (JSONObject) customA1.getObject();
        AA aa2 = JSONObject.toJavaObject(object, AA.class);
        Assert.isTrue(aa2 instanceof AA);

        /**
         * 重塑对象
         */


    }
    @Test
    public void spilt(){
        String[] hellos = StringUtils.split("hello", ",");
        Assert.isTrue(hellos!=null && hellos[0].equalsIgnoreCase("hello"));
        String[] hellos1 = org.springframework.util.StringUtils.split("hello", ",");
        Assert.isTrue(hellos1==null );

    }
}
