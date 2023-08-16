package com.evoting.evoting.system.dto.response;

import com.evoting.evoting.system.dto.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@lombok.Data
public class Response {
    private String code;
    private String message;
    private Data data;
}
