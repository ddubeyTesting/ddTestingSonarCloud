package com.zemoso.figma.growthcapital.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_credit_details")
public class UserCreditDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "available_credit")
    private BigDecimal availableCredit = BigDecimal.valueOf(0.0d);

    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount = BigDecimal.valueOf(0.0d);

    private String currency;

}
