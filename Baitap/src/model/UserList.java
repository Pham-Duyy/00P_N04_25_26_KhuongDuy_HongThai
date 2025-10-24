package model;

public class UserList {
    private String username;
    private String email;
    private String phone;

    // Constructor
    public UserList(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }

    // Hiển thị thông tin người dùng
    public void displayInfo() {
        System.out.println("Username: " + username);
        System.out.println("Email   : " + email);
        System.out.println("Phone   : " + phone);
    }

    // Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
