package utiljson;

public class TupPair<A,B> {
    private final A a;
    private final B b;

    public TupPair(A a, B b){
        this.a = a;
        this.b = b;
    }
    public A getA(){
        return a;
    }
    public B getB(){
        return b;
    }
}
