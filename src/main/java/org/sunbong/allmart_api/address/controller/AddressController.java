package org.sunbong.allmart_api.address.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sunbong.allmart_api.address.dto.AddressDTO;
import org.sunbong.allmart_api.address.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 주소 등록
    @PostMapping
    public ResponseEntity<AddressDTO> saveAddress(@RequestBody AddressDTO addressDTO) {
        AddressDTO savedAddress = addressService.saveAddress(addressDTO);
        return ResponseEntity.ok(savedAddress);
    }

    // 모든 주소 조회
    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }
}
