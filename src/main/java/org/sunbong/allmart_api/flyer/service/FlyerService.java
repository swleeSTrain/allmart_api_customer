package org.sunbong.allmart_api.flyer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbong.allmart_api.flyer.repository.FlyerRepository;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FlyerService {

    private final FlyerRepository flyerRepository;


}
