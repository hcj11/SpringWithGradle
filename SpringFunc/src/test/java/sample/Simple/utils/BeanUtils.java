package sample.Simple.utils;

import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
class A {
    private String name;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class B {
    private String name;
    private Tmp tmp;
    private Date date;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class Tmp {
    private Integer tmpAge;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
class C {
    private String name;
    private Tmp tmp;
    private Date date;

}
@Slf4j
public class BeanUtils {
    /**
     */
    @Test
    public void try1() {
        A hcj = A.builder().name("hcj").build();
        B bNewHcj = new B();
        org.springframework.beans.BeanUtils.copyProperties(hcj, bNewHcj);
        Assert.isTrue(hcj.getName() == bNewHcj.getName());
    }

    @Test
    public void try2() {
        Tmp build = Tmp.builder().tmpAge(1).build();
        B hcjWithB = B.builder().name("hcjWithB").tmp(build).build();
        C hcjWithC = new C();
        org.springframework.beans.BeanUtils.copyProperties(hcjWithB, hcjWithC);
        Assert.isTrue(hcjWithB.getName() == hcjWithC.getName());
        Assert.isTrue(build == hcjWithB.getTmp() && hcjWithB.getTmp() == hcjWithC.getTmp());
        /**
         */
        build.setTmpAge(3);
        Assert.isTrue(hcjWithB.getTmp().getTmpAge().equals(hcjWithB.getTmp().getTmpAge()));
        Assert.isTrue(hcjWithB.getTmp().getTmpAge() == hcjWithB.getTmp().getTmpAge());
        /**
         */
        hcjWithB.setTmp(null);
        Assert.isNull(hcjWithB.getTmp());
        Assert.notNull(hcjWithC.getTmp());

    }

    @Test
    public void try3() {
        B hcjWithB = B.builder().name("hcjWithB").tmp(Tmp.builder().tmpAge(1).build()).build();
        C hcjWithC = new C();
        org.springframework.beans.BeanUtils.copyProperties(hcjWithB, hcjWithC);
        hcjWithC.setTmp(Tmp.builder().tmpAge(1).build());

        hcjWithB.getTmp().setTmpAge(2);
        Assert.isTrue(hcjWithB.getTmp().getTmpAge() != hcjWithC.getTmp().getTmpAge());
        Assert.isTrue(!hcjWithB.getTmp().getTmpAge().equals(hcjWithC.getTmp().getTmpAge()));

        hcjWithB.setTmp(null);
        Assert.notNull(hcjWithC.getTmp());

    }

    @Test
    public void try4(){
        B hcjWithB = B.builder().name("hcjWithB").tmp(Tmp.builder().tmpAge(1).build()).date(new Date()).build();
        C hcjWithC = new C();
        org.springframework.beans.BeanUtils.copyProperties(hcjWithB, hcjWithC);
        log.info("{}",hcjWithC.getDate());
        Assert.notNull(hcjWithC.getDate());


    }

}
