package org.sunbong.allmart_api.member.controller.advice;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sunbong.allmart_api.member.exception.MemberTaskException;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class MemberControllerAdvice {
    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<Map<String,Object>> ex(MemberTaskException ex) {
        log.error("========================================================");
        StackTraceElement[] arr = ex.getStackTrace();
        for (StackTraceElement ste : arr) {
            log.error(ste.toString());
        }
        log.error("========================================================");
        Map<String,Object> msgMap = new HashMap<>();
        msgMap.put("message", ex.getMessage());

        return ResponseEntity.status(ex.getStatus()).body(msgMap);
    }
}
