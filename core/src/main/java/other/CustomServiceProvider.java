package other;

import java.util.ServiceLoader;

interface ServiceProvider {
    void sayHello();
}

public class CustomServiceProvider implements ServiceProvider {
    @Override
    public void sayHello() {
        System.out.println("hello world");
    }

    public static void main(String[] args) {
        ServiceLoader<ServiceProvider> load = ServiceLoader.load(ServiceProvider.class);
        for (ServiceProvider serviceProvider : load) {
            serviceProvider.sayHello();
        }

    }
}

