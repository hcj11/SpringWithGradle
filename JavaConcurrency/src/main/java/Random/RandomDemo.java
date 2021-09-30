package Random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDemo {
    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();
    }

    private static Random random() {
        return RandomNumberGeneratorHolder.randomNumberGenerator;
    }

    public static void main(String[] args) {
        Random random = new Random(25);
        // [0-3) [1,4)
        int i = random.nextInt(3) + 1;
        //  0001
        //  0100
        int i1 = 1 << 2;
        System.out.println(i1);
        ThreadLocalRandom current = ThreadLocalRandom.current();
        int i2 = current.nextInt(1);
        Random random1 = random();


    }

}
