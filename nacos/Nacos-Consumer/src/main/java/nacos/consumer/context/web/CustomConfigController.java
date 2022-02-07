//package nacos.consumer.context.web;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//@Configuration
//@Slf4j
//@Data
//@RequestMapping("consumer")
//@RestController
//public class CustomConfigController {
//
//    @LoadBalanced
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    @Value("${useLocalCache:false}")
//    public Boolean isuseLocalCache;
//
//
//    /**
//     *  nacos��Ӧ�÷�����
//     */
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
//    public ResponseEntity echo(@PathVariable String str) {
//        ResponseEntity forObject = restTemplate.getForObject("http://service-provider/provider/get/" + str, ResponseEntity.class);
//        log.info("{}",forObject);
//        return forObject;
//    }
//
//
//}
//
