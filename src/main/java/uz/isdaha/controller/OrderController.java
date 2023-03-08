package uz.isdaha.controller;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.isdaha.enums.OrderStatus;
import uz.isdaha.enums.PaymentMethod;
import uz.isdaha.payload.request.OrderPageableRequest;
import uz.isdaha.payload.request.OrderRequest;
import uz.isdaha.payload.request.OrderUpdateRequest;
import uz.isdaha.service.order.OrderService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_MANAGER') or  hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class OrderController {

    private final OrderService orderService;


    @PostMapping()
    public ResponseEntity<?> add(@RequestBody @Validated OrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return orderService.get(id);
    }

//    @GetMapping
//    public ResponseEntity<?> getAll(Pageable pageable) {
//        return orderService.(pageable);
//    }

    @GetMapping("history")
    public ResponseEntity<?> getHistory() {
        return orderService.history();
    }


    // deploying problem in heroku
    @GetMapping("/report")
    public ResponseEntity<Resource> downloadReportByExcel(@RequestParam("from") Long from,
                                                          @RequestParam("to") Long to) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String fileName = formatter.format(new Date(from)) + "-" + formatter.format(new Date(to)) + ".xlsx";
        InputStreamResource file = new InputStreamResource(orderService.reportOrder(new Timestamp(new Date(from).getTime()), new Timestamp(new Date(to).getTime())));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }

    @GetMapping("generate/{id}")
    public ResponseEntity<?> generateCheck(@PathVariable Long id) throws IOException, WriterException {
        return orderService.getByIdForCheck(id);
    }

//   id

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody @Validated OrderUpdateRequest request) {
        return orderService.update(id, request);
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
//        return orderService.delete(id);
//    }


    @PostMapping("all")
    public ResponseEntity<?> getAll(@RequestBody OrderPageableRequest request) {
        return orderService.getAll(request);
    }
}
