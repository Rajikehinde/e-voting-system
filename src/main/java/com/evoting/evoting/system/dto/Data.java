package com.evoting.evoting.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@lombok.Data
@Builder
public class Data {
    private String name;
    private String cardNo;
}
