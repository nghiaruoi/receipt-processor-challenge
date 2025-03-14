package com.spring.receiptprocessorchallenge.Controller;

import com.spring.receiptprocessorchallenge.Data.Receipt;
import com.spring.receiptprocessorchallenge.Exception.ResourceNotFoundException;
import com.spring.receiptprocessorchallenge.Service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> process(@RequestBody Receipt receipt) {

        try {
            UUID receiptId = receiptService.saveReceipt(receipt).getReceiptId();
            Map<String, String> response = new HashMap<>();
            response.put("id", receiptId.toString());
            return ResponseEntity.ok(response);
        } catch (TransactionSystemException error) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", error.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/all")
    public List<Receipt> getAllReceipts() {
        return receiptService.getAllReceipts();
    }

    @GetMapping("/{receiptId}/points")
    public ResponseEntity<Map<String, Object>> getReceiptPoints(@PathVariable("receiptId") String receiptId) {
        try {
            int points = receiptService.calculatePoints(receiptId);
            Map<String, Object> response = new HashMap<>();
            response.put("points", points);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException error) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", error.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
