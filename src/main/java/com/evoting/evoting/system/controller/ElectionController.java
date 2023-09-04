package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.request.ElectionRequest;
import com.evoting.evoting.system.service.serviceForElection.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ElectionController {
    @Autowired
    private ElectionService electionService;
    @PostMapping("/time")
    public boolean Time(@RequestBody ElectionRequest electionRequest){
        return electionService.Time(electionRequest);
    }
}
