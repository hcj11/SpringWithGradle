package context.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestBean implements ITestBean{
    private String age;
    private String name;
    /**
     * ����
     */
    private ITestBean spouse;

}
