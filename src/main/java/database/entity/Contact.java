package database.entity;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    @OneToOne()
    @JoinColumn(name = "trade_id")
    private Trade trade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Contact() {
    }

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Contact(String name, String phone, String email, Trade trade) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.trade = trade;
    }

    public Contact(String name, String phone, String email, Address address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Contact(String name, String phone, String email, Trade trade, Address address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.trade = trade;
        this.address = address;
    }

    public Contact(String name, String phone, String email, String description, String comments, Trade trade, Address address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.comments = comments;
        this.trade = trade;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", comments='" + comments + '\'' +
                ", trade=" + trade +
                ", address=" + address +
                '}';
    }
}
