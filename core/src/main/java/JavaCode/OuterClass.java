package JavaCode;

interface ShareInterface {
    void doThis();
}

public class OuterClass {
    private Integer age = 1;

    //    �ڲ���
    public class InnerClass {
        private String name = "hcj";

        public void compute() {
            System.out.println(OuterClass.this.age++);

            new ShareInterface() {
                @Override
                public void doThis() {

                }
            };

        }
    }


}

// �ǹ�����
class InnerCls {
    private Long otherMsg;

    public InnerCls() {
//        OuterClass.InnerClass innerClass = new OuterClass.InnerClass();
//        String name = innerClass.name;
    }

    public void gotIt() {

    }

}
