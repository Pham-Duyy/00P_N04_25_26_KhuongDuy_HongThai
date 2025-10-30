public class threadexample extends thread {
    public static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        threadexample threadExp = new threadexample();
        threadExp.start();
        threadExp.join();  // đợi thread chạy xong

        System.out.println("Count: " + count);
    }

    public void run() {
        count++;
    }
}
