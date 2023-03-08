package uz.isdaha.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PageProducer {
    ResponseEntity<?> pageOf(Pageable pageable);
}
