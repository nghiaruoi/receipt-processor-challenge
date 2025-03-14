package com.spring.receiptprocessorchallenge.Service;

import com.spring.receiptprocessorchallenge.Data.Item;
import com.spring.receiptprocessorchallenge.Data.Receipt;
import com.spring.receiptprocessorchallenge.Data.ReceiptRepository;
import com.spring.receiptprocessorchallenge.Exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
public class ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Transactional
    public Receipt saveReceipt(Receipt receipt) {
        receipt.getItems().forEach(item -> item.setReceipt(receipt));  // Ensure each item is linked to the receipt
        return receiptRepository.save(receipt);
    }

    @Transactional
    public int calculatePoints(UUID receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found"));

//        System.out.println(receipt);
        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name.
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
        System.out.println("Rule 1");
        System.out.println(points);

        // Rule 2: 50 points if the total is a round dollar amount.
        if (receipt.getTotal().remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
            System.out.println("Rule 2");
            System.out.println(points);
        }

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (receipt.getTotal().remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
            System.out.println("Rule 3");
            System.out.println(points);
        }

        // Rule 4: 5 points for every two items on the receipt.
        if (receipt.getItems() != null) {
            points += (receipt.getItems().size() / 2) * 5;
            System.out.println("Rule 4");
            System.out.println(points);
        }

        // Rule 5: If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up.
        if (receipt.getItems() != null) {
            for (Item item : receipt.getItems()) {
                if (item.getShortDescription().trim().length() % 3 == 0) {
                    BigDecimal itemPoints = item.getPrice().multiply(new BigDecimal("0.2")).setScale(0, RoundingMode.UP);
                    points += itemPoints.intValue();
                }
            }
            System.out.println("Rule 5");
            System.out.println(points);
        }

        // Rule 7: 6 points if the day in the purchase date is odd.
        if (receipt.getPurchaseDate().getDayOfMonth() % 2 != 0) {
            points += 6;
            System.out.println("Rule 7");
            System.out.println(points);
        }

        // Rule 8: 10 points if the time of purchase is between 2:00pm and 4:00pm.
        if (receipt.getPurchaseTime().isAfter(java.time.LocalTime.of(14, 0)) &&
                receipt.getPurchaseTime().isBefore(java.time.LocalTime.of(16, 0))) {
            points += 10;
            System.out.println("Rule 8");
            System.out.println(points);
        }

        return points;
    }
}