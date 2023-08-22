package com.evoting.evoting.system.controller;

import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.dto.request.VotersRequest;
import com.evoting.evoting.system.service.serviceForVoters.VotersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class VoterController {
    @Autowired
    private VotersService votersService;

    @PostMapping("/voter/registration")
    public Response registerVoters (@RequestBody VotersRequest votersRequest){
        return votersService.registerVoters(votersRequest);
    }
    @GetMapping("/fetchVoters")
    public List<Response> fetchAllVoters(){
        return votersService.fetchAllVoters();
    }
    @PutMapping("/updateVoter")
    public Response updateVoters(@RequestBody VotersRequest votersRequest){
        return votersService.updateVoters(votersRequest);
    }
    @DeleteMapping("/delete/voter{delete}")
    public Response delete(@PathVariable("delete")Long id){
        return votersService.delete(id);
    }
}
