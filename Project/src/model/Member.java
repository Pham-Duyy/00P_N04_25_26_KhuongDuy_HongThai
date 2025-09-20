package model;
public class Member{
    private String name;
    private String id;
    private String email;
    private String phone;
    private String address;
    private String membershipDate;
    private String membershipType;
    // Constructor
    
    public Member(String name, String id, String email, String phone, String address, String membershipDate, String membershipType) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipDate = membershipDate;
        this.membershipType = membershipType;

    }
    // Hiển thị thông tin member
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Address: " + address);
        System.out.println("Membership Date: " + membershipDate);
        System.out.println("Membership Type: " + membershipType);
    }
    // Getter cho name
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public String getMembershipDate() {
        return membershipDate;
    }
    public String getMembershipType() {
        return membershipType;
    }

    // Setter cho name
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setMembershipDate(String membershipDate) {
        this.membershipDate = membershipDate;
    }
    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
    
}
