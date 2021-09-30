package Basic;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class CarFactoryBean implements FactoryBean<Car> {

    @Override
    public Car getObject() throws Exception {
        Car car = new Car();
        car.setName("bwm");
        car.setWheel("4");
        car.setTags("good car");
        return car;
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
