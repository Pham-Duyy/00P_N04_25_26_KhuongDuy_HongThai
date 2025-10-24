package callback;

public class Caller {
    private Incrementable callbackReference;

    public Caller(Incrementable cbr) {
        callbackReference = cbr;
    }

    public void go() {
        System.out.println("Caller: đang gọi lại...");
        callbackReference.increment();
    }
}
