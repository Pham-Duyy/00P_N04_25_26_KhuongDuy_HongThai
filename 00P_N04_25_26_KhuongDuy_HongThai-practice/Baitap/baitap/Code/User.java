package Code;
public class User {
    private String username;
    private String email;
    private String password;

    // Các contructor
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

    }
     // Getter và Setter
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
        if (email.contains("@") && email.contains(".")) {
            this.email = email;
        } else {
            System.out.println("Email không hợp lệ!");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   // Phương thức để đặt lại mật khẩu
    public void forgotPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password has been reset successfully.");
    }
@Override
    public String toString() {
        return "User:" +
                "username :'" + username + '\'' +
                ", email :'" + email + '\'' +
                ", password :'" + password + '\'' +
                ":";
    }
}