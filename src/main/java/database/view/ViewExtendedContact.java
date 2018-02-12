package database.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Table(name = "view_extended_contacts")
public class ViewExtendedContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private int contactId;

    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "name")
    private String name;

    @Column(name = "trade")
    private String trade;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "street")
    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    public ViewExtendedContact() {
    }

    public int getContactId() {
        return contactId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public String getName() {
        return name;
    }

    public String getTrade() {
        return trade;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getDescription() {
        return description;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "ViewExtendedContact{" +
                "contactId=" + contactId +
                ", addressId=" + addressId +
                ", name='" + name + '\'' +
                ", trade='" + trade + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", description='" + description + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
