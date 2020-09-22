package com.vulenhtho.mrssso.controller.admin;

import com.vulenhtho.mrssso.dto.BillDTO;
import com.vulenhtho.mrssso.dto.ReportDTO;
import com.vulenhtho.mrssso.dto.UpdateBillDTO;
import com.vulenhtho.mrssso.dto.request.AddAnItemIntoBillDTO;
import com.vulenhtho.mrssso.dto.request.BillFilterRequest;
import com.vulenhtho.mrssso.dto.response.PageBillsResponse;
import com.vulenhtho.mrssso.dto.response.ShortInfoBillResponse;
import com.vulenhtho.mrssso.entity.enumeration.BillStatus;
import com.vulenhtho.mrssso.entity.enumeration.PaymentMethod;
import com.vulenhtho.mrssso.security.exception.ObjectIsNullException;
import com.vulenhtho.mrssso.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN') or hasRole('SALE')")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/bills")
    public ResponseEntity<PageBillsResponse> getListBill(@RequestParam(required = false) String status, @RequestParam(required = false) String search
            , @RequestParam(required = false) String paymentMethod, @RequestParam(required = false) String sort, @RequestParam(required = false
            , defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "5") Integer size) {

        BillFilterRequest billFilterRequest = new BillFilterRequest(sort, search, page, size);
        billFilterRequest.setStatus(status != null ? BillStatus.valueOf(status) : null);
        billFilterRequest.setPaymentMethod(paymentMethod != null ? PaymentMethod.valueOf(paymentMethod) : null);

        try {
            Page<ShortInfoBillResponse> billResponsePage = billService.getListShortInfoBill(billFilterRequest);
            PageBillsResponse billsResponse = new PageBillsResponse(billResponsePage.getContent(), billResponsePage.getTotalPages(), billResponsePage.getNumber());
            return ResponseEntity.ok(billsResponse);
        } catch (ObjectIsNullException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/bill/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            billService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("not found bill with id:" + id.toString());
        }
    }

    @DeleteMapping("/bills")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
        try {
            billService.delete(ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("not found bills with id:" + ids.toString());
        }
    }

    @GetMapping("/bill")
    public ResponseEntity<BillDTO> getBillDetail(@RequestParam Long id) {
        BillDTO billDTO = billService.getBillDetail(id);
        if (billDTO != null) {
            return ResponseEntity.ok(billDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/bill")
    public ResponseEntity<?> updateBill(@RequestBody UpdateBillDTO updateBillDTO) {
        try {
            billService.updateBillByAdmin(updateBillDTO);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getStatusText(), e.getStatusCode());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bill/addItem")
    public ResponseEntity<?> addItemToBill(@RequestBody AddAnItemIntoBillDTO addAnItemIntoBillDTO) {
        try {
            billService.addItemIntoBill(addAnItemIntoBillDTO);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getStatusText(), e.getStatusCode());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bill/reportByMonthAndYear")
    public ResponseEntity<ReportDTO> getReportByMonthAndYear(@RequestParam Integer month, @RequestParam Integer year) {
        return ResponseEntity.ok(billService.getReportByMonthAndYear(month, year));
    }

    @GetMapping("/bill/reportByYear")
    public ResponseEntity<ReportDTO> getReportByYear(@RequestParam Integer year) {
        return ResponseEntity.ok(billService.getReportByYear(year));
    }

}
