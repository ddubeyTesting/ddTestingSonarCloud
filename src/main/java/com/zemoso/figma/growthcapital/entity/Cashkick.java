package com.zemoso.figma.growthcapital.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cashkick_details")
public class Cashkick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true)
    private String name;

    @Column(name = "total_financed")
    private BigDecimal totalFinanced = BigDecimal.valueOf(0.0d);

    @Column(name = "total_received")
    private BigDecimal totalReceived = BigDecimal.valueOf(0.0d);

    private Integer rate = 0;

    private String status;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING, pattern = "MMM dd,yyyy")
    private Date startdate;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING, pattern = "MMM dd,yyyy")
    private Date maturity;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "cashkick_contracts",
            joinColumns = @JoinColumn(name = "cashkick_id"),
            inverseJoinColumns = @JoinColumn(name = "contract_id"))
    private List<Contract> contracts;
}
