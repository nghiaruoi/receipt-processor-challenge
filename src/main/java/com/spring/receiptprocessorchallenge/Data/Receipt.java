package com.spring.receiptprocessorchallenge.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "receipt")
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "receipt_id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID receiptId;

    @Column(name = "retailer")
    @JsonProperty("retailer")
    @NotNull(message = "The receipt is invalid.")
    private String retailer;

    @Column(name = "purchase_date")
    @JsonProperty("purchaseDate")
    @NotNull(message = "The receipt is invalid.")
    private LocalDate purchaseDate;

    @Column(name = "purchase_time")
    @JsonProperty("purchaseTime")
    @NotNull(message = "The receipt is invalid.")
    private LocalTime purchaseTime;

    @Column(name = "total")
    @JsonProperty("total")
    @NotNull(message = "The receipt is invalid.")
    private BigDecimal total;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonProperty("items")
    @NotNull(message = "The receipt is invalid.")
    private List<Item> items = new ArrayList<>();

}