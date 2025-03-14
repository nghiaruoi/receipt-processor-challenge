package com.spring.receiptprocessorchallenge.Controller;

import com.spring.receiptprocessorchallenge.Data.Receipt;
import com.spring.receiptprocessorchallenge.Service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/process")
    public UUID process(@RequestBody Receipt receipt) {
        return receiptService.saveReceipt(receipt).getReceiptId();
    }

    @GetMapping("/{receiptId}/points")
    public ResponseEntity<Integer> getReceiptPoints(@PathVariable("receiptId") UUID receiptId) {
        try {
            int points = receiptService.calculatePoints(receiptId);
            return ResponseEntity.ok(points);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
