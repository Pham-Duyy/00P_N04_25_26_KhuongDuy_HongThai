package callback;

public class TestCallback {
    public static void main(String[] args) {
        Callee callee = new Callee();
        Caller caller = new Caller(callee);

        // Gọi go() 10 lần
        for (int i = 0; i < 10; i++) {
            caller.go();
        }
    }
}
