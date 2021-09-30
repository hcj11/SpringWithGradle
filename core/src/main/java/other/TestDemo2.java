package other;
class Hello{
    public synchronized void aa(){
        System.out.println("hello1 ");
    }
    public void aaa(){
        synchronized (this){
            System.out.println("hello ");
        }
    }
}
public class TestDemo2 {
    public static void main(String[] args) {
    }
}
