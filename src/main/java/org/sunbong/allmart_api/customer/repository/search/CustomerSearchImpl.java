package org.sunbong.allmart_api.customer.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sunbong.allmart_api.common.dto.PageRequestDTO;
import org.sunbong.allmart_api.common.dto.PageResponseDTO;
import org.sunbong.allmart_api.customer.domain.Customer;
import org.sunbong.allmart_api.customer.dto.CustomerListDTO;

public class CustomerSearchImpl implements CustomerSearch {


    @Override
    public Page<Customer> list(Pageable pageable) {



        return null;
    }

    @Override
    public PageResponseDTO<CustomerListDTO> listByCno(Long cno, PageRequestDTO pageRequestDTO) {
        return null;
    }
}
