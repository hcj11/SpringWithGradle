package Basic;

import org.springframework.stereotype.Component;
@Component
class D{
    private C c;
    public D(C c){
        this.c=c;
    }
}
@Component
public class C {
    private D d;
    public C(D d){
     this.d=d;
    }
}

