package classLoader;

import lombok.SneakyThrows;

import java.io.InputStream;

public class CustomClassLoader {


    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ClassLoader classLoader = new ClassLoader() {
            @SneakyThrows
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                //
                String s1 = name.substring(name.lastIndexOf(".") + 1) + ".class";
                System.out.println("===========" + s1);
                InputStream cust = getClass().getResourceAsStream(s1);
                if (cust == null) {
                    return super.loadClass(name);
                }
                int read = cust.available();
                byte[] s = new byte[read];
                cust.read(s);
                return defineClass(name, s, 0, s.length);
            }
        };
        Object o = classLoader.loadClass("classLoader.CustomClassLoader").newInstance();
        System.out.println(o instanceof CustomClassLoader);

    }
}
