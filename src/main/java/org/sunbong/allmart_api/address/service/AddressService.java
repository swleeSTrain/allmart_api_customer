package org.sunbong.allmart_api.address.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sunbong.allmart_api.address.domain.Address;
import org.sunbong.allmart_api.address.dto.AddressDTO;
import org.sunbong.allmart_api.address.repository.AddressRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    // 주소 저장
    public AddressDTO saveAddress(AddressDTO addressDTO) {
        // AddressDTO -> Address Entity 변환
        Address address = Address.builder()
                .postcode(addressDTO.getPostcode())
                .roadAddress(addressDTO.getRoadAddress())
                .detailAddress(addressDTO.getDetailAddress())
                .fullAddress(addressDTO.getRoadAddress() + " " + addressDTO.getDetailAddress())
                .build();

        // 데이터베이스에 저장
        Address savedAddress = addressRepository.save(address);

        // 저장된 Entity -> AddressDTO 변환
        return AddressDTO.builder()
                .addressID(String.valueOf(savedAddress.getAddressID()))
                .postcode(savedAddress.getPostcode())
                .roadAddress(savedAddress.getRoadAddress())
                .detailAddress(savedAddress.getDetailAddress())
                .fullAddress(savedAddress.getFullAddress())
                .build();
    }

    // 모든 주소 조회
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(address -> AddressDTO.builder()
                        .addressID(String.valueOf(address.getAddressID()))
                        .postcode(address.getPostcode())
                        .roadAddress(address.getRoadAddress())
                        .detailAddress(address.getDetailAddress())
                        .fullAddress(address.getFullAddress())
                        .build())
                .collect(Collectors.toList());
    }
}
