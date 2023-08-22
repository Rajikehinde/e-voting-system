package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.request.CastVoteRequest;
import com.evoting.evoting.system.dto.request.ViewResultResponse;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.service.serviceForVoteCount.VoteCountService;
import com.evoting.evoting.system.service.serviceForVoters.VotersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VoteCountController {

   private final VoteCountService voteCountService;
   private final VotersService votersService;

    public VoteCountController(VoteCountService voteCountService, VotersService votersService) {
        this.voteCountService = voteCountService;
        this.votersService = votersService;
    }

    @PostMapping("presidency/vote")
    public ResponseEntity<?> castVoteForPresidency(@RequestBody CastVoteRequest castVoteRequest){
        Response castVoteResponse = voteCountService.castVoteForPresidency(castVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(castVoteResponse);
    }
    @PostMapping("governor/vote")
    Response castVoteForGovernor(@RequestBody CastVoteRequest castVoteRequest){
        Response castVoteResponse = voteCountService.castVoteForGovernor(castVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(castVoteResponse).getBody();
    }
    @PostMapping("senate/vote")
    Response castVoteForSENATE(@RequestBody CastVoteRequest castVoteRequest){
        Response castVoteResponse = voteCountService.castVoteForSENATE(castVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(castVoteResponse).getBody();
    }
    @PostMapping("houseOfAssembly/vote")
    Response castVoteForHOUSE_OF_ASSEMBLY(@RequestBody CastVoteRequest castVoteRequest){
        Response castVoteResponse = voteCountService.castVoteForHOUSE_OF_ASSEMBLY(castVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(castVoteResponse).getBody();
    }
    @PostMapping("houseOfRep/vote")
    Response castVoteForHOUSE_OF_REPRESENTATIVE(@RequestBody CastVoteRequest castVoteRequest){
        Response castVoteResponse = voteCountService.castVoteForHOUSE_OF_REPRESENTATIVE(castVoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(castVoteResponse).getBody();
    }
    @GetMapping("presidential/result")
    ViewResultResponse viewPresidentialResultInPercentage(){
        return voteCountService.viewPresidentialResultInPercentage();
    }
    @GetMapping("governorship/result")
    ViewResultResponse viewGovernorshipResultInPercentage(){
        return voteCountService.viewGovernorshipResultInPercentage();
    }
    @GetMapping("rep/result")
    ViewResultResponse viewHouseOfRepresentativeResultInPercentage(){
        return voteCountService.viewHouseOfRepresentativeResultInPercentage();
    }
    @GetMapping("senatorial/result")
    ViewResultResponse viewSenateResultInPercentage(){
        return voteCountService.viewSenateResultInPercentage();
    }
    @GetMapping("assembly/result")
    ViewResultResponse viewHouseOfAssemblyResultInPercentage(){
        return voteCountService.viewHouseOfAssemblyResultInPercentage();
    }

}
