package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class HexToCharTransformerTests {
    // when  to  transform the acsii , can use it  ex:  1234xdf
    private int HexToChar(String a) {
        return Integer.parseInt(a, 16);
    }

    private String CharArrayToHex(int a) {
        return Integer.toString(a & 0xff, 16);
    }
    @Test
    public void a(){
        // data to encode and use the base64  to store in the bit .
        Base64.getEncoder().encode("".getBytes(StandardCharsets.UTF_8));

    }
    @Test
    public void others4() {
        int i = 51;
        String s = Integer.toString(i, 16);
        int i1 = Integer.parseInt(s, 16);
        Assertions.assertEquals(i, i1);
        char i2 = (char) i;
        System.out.println(i2);
    }

    @Test
    public void others3() {
        // 1234xdf
        String content = "Tutorialspoint";
        String s = CharArrayToHexTests(content.toCharArray());
        log.info("{}", s);
        String s1 = HexToCharArrayTests(s.toCharArray());
        Assertions.assertEquals(content, s1);
        // hex -> string
        // 32

    }

    @Test
    public void others2() {
        int a = 276;
        int b = 788;
        Assertions.assertEquals(CharArrayToHex(a), CharArrayToHex(b));
        Assertions.assertEquals(HexToChar(CharArrayToHex(a)), HexToChar(CharArrayToHex(b)));
    }

    @Test
    public void others1() {
        int maxValue = (int) Character.MAX_VALUE;
        System.out.println(maxValue);
        int i = maxValue & 0xff;
        Assertions.assertEquals(i, 0xff);
    }

    @Test
    public void load1() {
        // 972 -> (204 &0xff,16)=CC ,
        String a = "后纯碱";
        // and char maxLength =16
//        Character.class;
        // 788  -> 14 (radix 16)
        // 788  -> 14 (radix 16)
        // 276 -> 14

        char[] chars = a.toCharArray();
        for (int i = 0; i < a.length(); i++) {
            log.info("{}", chars[0] & 0xff);
            ;
        }
        char aa = '1';

    }

    @Test
    public void load() {
        int i = 127;
        String s = Integer.toString(i, 16);
        log.info("{}", s);
        int i1 = Integer.parseInt(s, 16);
        Assertions.assertTrue(i == i1);


    }

    private String HexToCharArrayTests(char[] arr) {
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < arr.length; i = i + 2) {
            stringBuilder2.append((char)Integer.parseInt(String.valueOf(arr[i])+String.valueOf(arr[i+1]), 16));
        }
        return stringBuilder2.toString();
    }

    private String CharArrayToHexTests(char[] arr) {
        // 32
        // char -> 16hex  int i =3 => char=51
        StringBuilder stringBuilder2 = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            stringBuilder2.append(Integer.toString(arr[i] & 0xff, 16));
        }
        return stringBuilder2.toString();
    }

}
