package other;

import java.util.ServiceLoader;

public class CustomProvider extends AbstractServiceProvider {

    @Override
    void sayHi() {
        System.out.println("hi");
    }
    public static void main(String[] args) {
        ServiceLoader<AbstractServiceProvider> load = ServiceLoader.load(AbstractServiceProvider.class);
        for (AbstractServiceProvider serviceLoader:load){
            serviceLoader.sayHi();
        }
    }
}
