package Concurrent;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyAndWriteDemo {
    private    CopyOnWriteArrayList<String> strings = new CopyOnWriteArrayList<>();

    public void put(){
        // 插入后，在启动任务，

        strings.stream().forEach(c->{
            System.out.println(c);
        });
        // 有延迟性，但是线程池可以确定 （方法是否执行结束）。
        ArrayList<String> list = Lists.<String>newArrayList();
        List<List<String>> partition = Lists.partition(list, 10);


    }

    public static void main(String[] args) {
    }
}
