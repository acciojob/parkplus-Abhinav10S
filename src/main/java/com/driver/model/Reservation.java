package com.driver.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auto ;

    private int numberOfHours ;
    @ManyToOne
    @JoinColumn
    private User  user ;
    @ManyToOne
    @JoinColumn
    private Spot spot ;

    @OneToOne(mappedBy = "reservation"  , cascade = CascadeType.ALL)
    private Payment payment ;

    public int getAuto() {
        return auto;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
