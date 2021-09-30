package CollectionDemo;

public class Demo {
    public static int solution(int money) {
        // money 必须>0
        // money 不能超过 Integer.MAX_VALUE
        int sum = 0;
        // 买桃子数
        int peaches = money * 3;

        // 核数
        int peachCore = peaches;
        int exchange = 0;
        // 核处理
        while (peachCore % 3 >= 1) { // 说明换到了至少 1 个桃子
            // 换桃子
            int changPeaches = peachCore % 3;
            exchange += changPeaches;
            peachCore = changPeaches;
        }

        sum = peaches + exchange;

        return sum;
    }

    public static void main(String[] args) {
        //
//        int solution = solution(135142857);
//        int i = 135142857 * 3  % 3;
//        System.out.println(i);
//        System.out.println(solution);
//        double ceil = Math.ceil(10 / 3);
//        System.out.println(ceil);
//        double floor = Math.floor(10 / 3);
//        System.out.println(floor);
//        Math.floorMod(10 ,3)
//        Math.floorDiv()


    }
}
