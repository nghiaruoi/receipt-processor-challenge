package com.spring.receiptprocessorchallenge.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "item")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id", columnDefinition = "BINARY(16)")
    private UUID itemId;

    @Column(name = "short_description")
    @JsonProperty("shortDescription")
    private String shortDescription;

    @Column(name = "price")
    @JsonProperty("price")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "receipt_id", columnDefinition = "BINARY(16)")
    private Receipt receipt;

}