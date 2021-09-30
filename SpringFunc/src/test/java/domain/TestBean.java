package domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestBean implements ITestBean{
    private String age;
    private String name;
    /**
     * ∑Ú»À
     */
    private ITestBean spouse;

}
