package org.example.entity;
import jakarta.persistence.Entity;

@Entity
public class Customer extends Person {

    private String membershipType;

    public Customer() {
    }

    public Customer(String name, String membershipType) {
        super(name);
        this.membershipType = membershipType;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }
}