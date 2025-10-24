package Test;
import Code.NameNumber;
import Code.NNCollection;

public class Main {
    public static void main(String[] args) {
        // Tạo đối tượng NNCollection
        NNCollection collection = new NNCollection();

        // Tạo một số đối tượng NameNumber và chèn vào collection
        NameNumber person1 = new NameNumber("Nguyen", "0123456789");
        NameNumber person2 = new NameNumber("Tran", "0987654321");
        NameNumber person3 = new NameNumber("Le", "0345678910");

        collection.insert(person1);
        collection.insert(person2);
        collection.insert(person3);

        // Tìm số điện thoại theo lastName
        System.out.println("Số điện thoại của Tran: " + collection.findNumber("Tran"));
        System.out.println("Số điện thoại của Le: " + collection.findNumber("Le"));
        System.out.println("Số điện thoại của Nguyen: " + collection.findNumber("Nguyen"));
        System.out.println("Số điện thoại của Khai: " + collection.findNumber("Khai"));
    }
}
