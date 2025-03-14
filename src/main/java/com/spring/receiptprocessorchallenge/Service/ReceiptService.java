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
import java.util.List;
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

    public Receipt getReceipt(String receiptId) {
        return receiptRepository.findById(UUID.fromString(receiptId))
                .orElseThrow(() -> new ResourceNotFoundException("No receipt found for that ID"));
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    public int calculatePoints(String receiptId) {

        Receipt receipt = getReceipt(receiptId);

        if (receipt == null) {
            throw new ResourceNotFoundException("No receipt found for that ID");
        }

        int points = 0;

        // Rule 1: One point for every alphanumeric character in the retailer name.
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        // Rule 2: 50 points if the total is a round dollar amount.
        if (receipt.getTotal().remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            points += 50;
        }

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (receipt.getTotal().remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) {
            points += 25;
        }

        // Rule 4: 5 points for every two items on the receipt.
        if (receipt.getItems() != null) {
            points += (receipt.getItems().size() / 2) * 5;
        }

        // Rule 5: If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up.
        if (receipt.getItems() != null) {
            for (Item item : receipt.getItems()) {
                if (item.getShortDescription().trim().length() % 3 == 0) {
                    BigDecimal itemPoints = item.getPrice().multiply(new BigDecimal("0.2")).setScale(0, RoundingMode.UP);
                    points += itemPoints.intValue();
                }
            }
        }

        // Rule 6: do not exist
        // Rule 7: 6 points if the day in the purchase date is odd.
        if (receipt.getPurchaseDate().getDayOfMonth() % 2 != 0) {
            points += 6;
        }

        // Rule 8: 10 points if the time of purchase is between 2:00pm and 4:00pm.
        if (receipt.getPurchaseTime().isAfter(java.time.LocalTime.of(14, 0)) &&
                receipt.getPurchaseTime().isBefore(java.time.LocalTime.of(16, 0))) {
            points += 10;
        }

        return points;
    }
}