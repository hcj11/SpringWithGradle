package context;

import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.type.StandardAnnotationMetadata;
import sample.*;
import sample.qualifier.Aqualifier;
import sample.qualifier.SubA;
import sample.qualifier.SubB;

import java.lang.annotation.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@Configuration
public class CompontScan {

    @Test
    public void match() {
        Pattern compile = Pattern.compile("[\\u4E00-\\u9FA5]+");
        boolean b = compile.matcher("nothing11").find();
        Assert.isTrue(b);
    }

    static class TestBean {
    }

    @Configuration
    @Import(Two.class)
    public static class One {

    }

    @Configuration
    public static class Two {
        @Bean
        public TestBean testBean() {
            return new TestBean();
        }
    }
    // useDefaultFilters=true ,default the other filter condition not take effect,
    @ComponentScan(
            useDefaultFilters = false,
            includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {FooService.SubFooService.class})},
            basePackages = "sample", scopedProxy = ScopedProxyMode.INTERFACES
    )
    public static class CustomB {

    }

    @Data
    @ComponentScan(useDefaultFilters = false,
            includeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {Aqualifier.class})}
            ,basePackages = "sample"
    )
    static class CustomAqualifier {
        @SubA
        @Autowired
        Aqualifier aqualifier;

        @SubB
        @Autowired
        Aqualifier aqualifie2;


    }
    // springboot ԭ���������Ƿ����Ч����
    @ComponentScan(useDefaultFilters = false,
            includeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = {CustomTypeFilter.class})},
            excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomA.class)},
            basePackageClasses = {SampleConfig.class}
    )
    static class CustomA {
        /**
         * ����ʼ��
         */
        public CustomTypeFilter.KCustomA kCustomA() {
            return new CustomTypeFilter.KCustomA();
        }

    }

    @Import(value = ImportRegistrar.class)
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EnableImportRegistrar {
    }

    @EnableImportRegistrar
    public static class internalImportBean {

    }

    static class Foo {
        public Foo() {
        }
    }

    static class Bar {
        public Foo foo;

        public Bar() {
        }

        public Bar(Foo foo) {
            this.foo = foo;
        }
    }

    @Configuration(proxyBeanMethods = false)
    public static class ConfigurationA {
        @Bean
        public Foo foo() {
            return new Foo();
        }

        @Bean
        public Bar bar() {
            return new Bar(foo());
        }
    }

    private DefaultListableBeanFactory bf;

    private AutowiredAnnotationBeanPostProcessor bpp;


    @BeforeEach
    public void setup() {
        bf = new DefaultListableBeanFactory();
        bf.registerResolvableDependency(BeanFactory.class, bf);
        bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(bf);
        bf.addBeanPostProcessor(bpp);
        bf.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
        bf.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE);
    }

    public interface Repository<T> {
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StrGenericsRepository implements Repository<String> {
        private String X;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class IntegerGenericsRepository implements Repository<Integer> {
        private Integer I;
    }

    /**
     * Bean
     */
    @Test
    public void ComponentConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfig.class);
        Assert.isFalse(context.getDefaultListableBeanFactory().containsSingleton("myBean"));
        print(context);
        SimpleConfig bean = context.getBean(SimpleConfig.class);
        One one = bean.getOne();
        One object = bean.getOneObjectFactory().getObject();
        List<One> collect = bean.getOneObjectProvider().stream().collect(Collectors.toList());

        Assert.isTrue(one==object);
        Assert.isTrue(collect.get(0)==object);
        System.out.println("---------------------------------------------------");
        print(context);
    }
    /**
     * FactoryBean
     */
    @Test
    public void componetConfig2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleConfig2.class);
        Assert.isFalse(context.getDefaultListableBeanFactory().containsSingleton("myBean"));
        printSingleton(context);
        SimpleConfig2 bean = context.getBean(SimpleConfig2.class);
        One one = bean.getOne();
        One object = bean.getOneObjectFactory().getObject();
        List<One> collect = bean.getOneObjectProvider().stream().collect(Collectors.toList());

        Assert.isTrue(one==collect.get(0));
        Assert.isTrue(collect.get(0)==object);
        System.out.println("---------------------------------------------------");
        printSingleton(context);
    }

    /**
     * ComponentScan  scan Component �����beanDefinition
     */
    @Test
    public void bareTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomTypeFilter.class);

        AnnotationConfigApplicationContext mock = Mockito.mock(AnnotationConfigApplicationContext.class);

        when( mock.getBean("customTypeFilter.KCustomA", CustomTypeFilter.KCustomA.class)).
                thenThrow(org.springframework.beans.factory.NoSuchBeanDefinitionException.class);

        print(context);
    }

    /**
     * ���
     */
    @Test
    public void testGenericsBasedFieldInjectionWithVariables() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GenericsBasedFieldInjectionWithSubVariables.class);
        Integer I = 100;
        String S = "hello";
        context.getBeanFactory().registerSingleton("stringvalue", S);
        context.getBeanFactory().registerSingleton("intvalue", I);

        StrGenericsRepository strGenericsRepository = new StrGenericsRepository(S);
        context.getBeanFactory().registerSingleton("stringRepo", strGenericsRepository);
        IntegerGenericsRepository integerGenericsRepository = new IntegerGenericsRepository(I);
        context.getBeanFactory().registerSingleton("intRepo", integerGenericsRepository);

        GenericsBasedFieldInjectionWithSubVariables annotatedBean = context.getBean(GenericsBasedFieldInjectionWithSubVariables.class);
        System.out.println(annotatedBean);

    }

    @Test
    public void testGenericsBasedFieldInjectionWithSubstitutedVariables() {
        Integer I = 100;
        String S = "hello";
        bf.registerSingleton("stringvalue", S);
        bf.registerSingleton("intvalue", I);
        Repository<String> sr = new StrGenericsRepository("hcj");
        bf.registerSingleton("stringRepo", sr);
        Repository<Integer> ir = new IntegerGenericsRepository(666);
        bf.registerSingleton("integerRepo", ir);


        RootBeanDefinition bd = new RootBeanDefinition(GenericsBasedFieldInjectionWithSubVariables.class);
        bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        bf.registerBeanDefinition("annotatedBean", bd);




        GenericsBasedFieldInjectionWithSubVariables annotatedBean = bf.getBean("annotatedBean", GenericsBasedFieldInjectionWithSubVariables.class);
        System.out.println(annotatedBean);

    }

    @Test
    public void UseFactoryBean() {
        /**
         * import  + componentScan + FactoryBean  + PostProcessor +
         */

    }
    /**
     *
     */
    @Test
    public void proxyMethodSetFalseBeans() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationA.class);
        Foo foo = context.getBean(Foo.class);
        Bar bar = context.getBean(Bar.class);
        Assert.isTrue(foo != bar.foo);
    }

    /**
     *
     * @importWare ,
     */
    @Test
    public void importCondition() {
        CustomImportWare.MetaHolder bean = new AnnotationConfigApplicationContext(CustomImport.ConfigurationOne.class, CustomImport.ConfigurationTwo.class)
                .getBean(CustomImportWare.MetaHolder.class);
        StandardAnnotationMetadata annotationMetadata = (StandardAnnotationMetadata) bean.getAnnotationMetadata();
        Assert.isTrue(annotationMetadata.getIntrospectedClass().equals(CustomImport.ConfigurationOne.class));
    }

    @Test
    public void configurationTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(internalImportBean.class);
        Assert.notNull(context.getBean("oneBeans"));
        Assert.notNull(context.getBean("customA"));

    }

    @Test
    public void importTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(One.class);
        printSingleton(context);
    }



    @Test
    public void scopedProxy() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomB.class);
        FooService bean = (FooService) context.getBean("fooService.SubFooService");
        Assert.isTrue(bean.foo(1).equalsIgnoreCase("bar"));
        print(context);
        Assert.isTrue(AopUtils.isJdkDynamicProxy(bean));
    }


    @Test
    public void demo1() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomA.class);
        Assert.isTrue(context.getDefaultListableBeanFactory().containsSingleton("customTypeFilter.KCustomA"));
        CustomTypeFilter.KCustomA bean = context.getBean("customTypeFilter.KCustomA", CustomTypeFilter.KCustomA.class);
        Assert.notNull(bean.getDependent());
    }

    @Test
    public void qualifier() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CustomAqualifier.class);
        CustomAqualifier bean = context.getBean(CustomAqualifier.class);
        Aqualifier aqualifier1 = bean.getAqualifier();
        Aqualifier aqualifier2 = bean.getAqualifie2();
        Assert.isTrue(aqualifier1!=aqualifier2);


    }

    public static void printSingleton(GenericApplicationContext context) {

        Stream.of(context.getDefaultListableBeanFactory().getSingletonNames()).forEach(System.out::println);
    }
    public static void print(ListableBeanFactory context) {

        Stream.of(context.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
