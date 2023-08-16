package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.request.CandidateRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.service.serviceForCandidate.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;
    @PostMapping("/candidate/registration")
    Response registerCandidate (@RequestBody CandidateRequest candidateRequest){
        return candidateService.registerCandidate(candidateRequest);
    }
    @GetMapping("/fetchAllCandidates")
    List<Response> fetchAllCandidates(){
        return candidateService.fetchAllCandidates();
    }
    @PutMapping("/candidate/update")
    Response updateCandidate(@RequestBody CandidateRequest candidateRequest){
        return candidateService.updateCandidate(candidateRequest);
    }
    @DeleteMapping("/deleteCandidate{delete}")
    Response delete(@PathVariable("delete") Long id){
        return candidateService.delete(id);
    }
}
