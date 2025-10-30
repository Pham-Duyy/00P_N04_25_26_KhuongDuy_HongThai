package Code;
import Test.TestRecursion;
import Test.TestTime;
import Test.TestUser;

public class App {
    public static void main(String[] args) throws Exception {
        TestUser.runTest();
        TestTime.main(args);
        TestRecursion.main(args);
    }
}
