//package com.spring.receiptprocessorchallenge.Service;
//
//import com.spring.receiptprocessorchallenge.Data.Item;
//import com.spring.receiptprocessorchallenge.Data.Receipt;
//import com.spring.receiptprocessorchallenge.Data.ReceiptDTO;
//import com.spring.receiptprocessorchallenge.Data.ReceiptRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class ReceiptServiceImplementation implements ReceiptService {
//
//    private ReceiptRepository receiptRepository;
//
//    @Override
//    public Receipt saveReceipt(ReceiptDTO receiptDTO) {
//        Receipt receipt = new Receipt();
//        receipt.setReceiptId(UUID.randomUUID());//generate UUID
//        receipt.setRetailer(receiptDTO.getRetailer());
//        receipt.setPurchase_date(receiptDTO.getPurchaseDate());
//        receipt.setPurchase_time(receiptDTO.getPurchaseTime());
//        receipt.setTotal(receiptDTO.getTotal());
//
//        List<Item> items = new ArrayList<>();
//        if (receiptDTO.getItems() != null) {
//            for (ReceiptDTO.ItemDTO itemDTO : receiptDTO.getItems()) {
//                Item item = new Item();
//                item.setPurchaseId(receipt.getPurchaseId());
//                item.setShortDescription(itemDTO.getShortDescription());
//                item.setPrice(itemDTO.getPrice());
//                item.setReceipt(receipt);
//                items.add(item);
//            }
//        }
//        receipt.setItems(items);
//
//        return receiptRepository.save(receipt);
//    }
//
//    @Override
//    public Receipt getReceiptById(UUID id) {
//        return receiptRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public List<Receipt> getAllReceipts() {
//        return (List<Receipt>) receiptRepository.findAll();
//    }
//
//    @Override
//    public boolean deleteReceiptById(UUID id) {
//        return false;
//    }
//}
