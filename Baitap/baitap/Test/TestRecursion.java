package Test;

import Code.Recursion;

public class TestRecursion {
    public static void main(String[] args) {
        // Kiểm tra hàm tính giai thừa
        int n = 5;
        int ketQuaGiaiThua = Recursion.factorial(n);
        System.out.println("Giai thừa của " + n + " là: " + ketQuaGiaiThua);

        // Kiểm tra hàm tính số Fibonacci
        int ketQuaFibonacci = Recursion.fibonacci(n);
        System.out.println("Số Fibonacci thứ " + n + " là: " + ketQuaFibonacci);

        // Kiểm tra hàm đảo ngược chuỗi
        String chuoi = "hello";
        String ketQuaDaoNguoc = Recursion.reverse(chuoi);
        System.out.println("Đảo ngược chuỗi '" + chuoi + "' là: " + ketQuaDaoNguoc);
    }
}