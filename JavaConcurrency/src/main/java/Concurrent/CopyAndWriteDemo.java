package Concurrent;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyAndWriteDemo {
    private    CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList<>();

    public void put(){
        // ���������������

        strings.stream().forEach(c->{
            System.out.println(c);
        });
        // ���ӳ��ԣ������̳߳ؿ���ȷ�� �������Ƿ�ִ�н�������
        ArrayList<String> list = Lists.<String>newArrayList();
        List<List<String>> partition = Lists.partition(list, 10);


    }

    public static void main(String[] args) {
    }
}
