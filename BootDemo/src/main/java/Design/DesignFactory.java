package Design;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

enum PriceType {
    A, B, C;
}

interface SendPrice {
    boolean support(PriceType priceType);

    void sendPrice();
}

@Component
class SendPriceA implements SendPrice {

    @Override
    public boolean support(PriceType priceType) {
        return priceType == PriceType.A;
    }

    @Override
    public void sendPrice() {
        System.out.println("发送A");
        ;
    }
}

@Component
class SendPriceB implements SendPrice {

    @Override
    public boolean support(PriceType priceType) {
        return priceType == PriceType.B;
    }

    @Override
    public void sendPrice() {
        System.out.println("发送B");
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
class Request {
    private PriceType priceType;
    private String userId;

    @Component
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static class RequestBuilder {
        private PriceType priceType;
        private String userId;

        public RequestBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public RequestBuilder priceType(PriceType priceType) {
            this.priceType = priceType;
            return this;
        }

        public Request builder() {
            String userId = this.userId;
            PriceType priceType = this.priceType;
            return new Request(priceType, userId);
        }
    }
}

@Slf4j
@Service
public class DesignFactory {
    @Autowired
    private List<SendPrice> sendPrices;
    @Autowired
    private ApplicationContext applicationContext;

    public Request makeUpRequest(PriceType a, String s){
        // 构建不同的builder
        Request.RequestBuilder bean = applicationContext.getBean(Request.RequestBuilder.class);
        log.info("{}",bean.hashCode());
        return bean.priceType(a).userId(s).builder();
    }

    public void getSendPrice(Request request) {
        for (SendPrice sendPrice : sendPrices) {
            if (sendPrice.support(request.getPriceType())) {
                sendPrice.sendPrice();
            }
        }
    }

}
