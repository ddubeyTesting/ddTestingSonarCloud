package com.zemoso.figma.growthcapital.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String type;

    @Column(name = "term_length")
    private Integer termLength;

    @Column(name = "per_payment")
    private BigDecimal perPayment = BigDecimal.valueOf(0.0d);

    private Integer rate = 0;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount = BigDecimal.valueOf(0.0d);


    @JsonIgnore
    @ManyToMany(mappedBy = "contracts")
    private List<Cashkick> cashkicks;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;
}
