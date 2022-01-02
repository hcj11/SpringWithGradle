package sample.gcJVM;

import org.junit.jupiter.api.Test;

public class CustomClassLoaderTest {

    class NetworkClassLoader extends ClassLoader {
        String host;
        int port;

        public Class findClass(String name) {
            byte[] b = loadClassData(name);
            return defineClass(name, b, 0, b.length);
        }

        private byte[] loadClassData(String name) {
            // load the class data from the connection
            return null;
        }
    }

    @Test
    public void test1(){

    }
    public static void main(String[] args) {
    }
}
