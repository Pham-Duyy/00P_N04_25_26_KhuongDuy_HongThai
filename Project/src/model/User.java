package model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;

    // Constructor
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Hiển thị thông tin user
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
    }

    // Kiểm tra đăng nhập
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    // Đăng xuất
    public void logout() {
        System.out.println(name + " đã đăng xuất.");
    }

    // Getter cho name
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    // Setter cho name
    public void setName(String name) {
        this.name = name;
        }
    public void setId(int id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
