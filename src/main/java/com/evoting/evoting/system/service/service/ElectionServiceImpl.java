//package com.evoting.evoting.system.service.service;
//
//import com.evoting.evoting.system.domain.Election;
//import com.evoting.evoting.system.dto.request.ElectionRequest;
//import com.evoting.evoting.system.dto.response.Response;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//public class ElectionServiceImpl implements ElectionService {
//    @Override
//    public Response voteStarting(ElectionRequest electionRequest) {
//        Election openingTime = new Election();
//        openingTime.setElectionName(electionRequest.getElectionName());
//    }
//    public Response setDate(ElectionRequest electionRequest){
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
//        String election = format.format(date);
//        System.out.println("election date: " + election);
//    }
//}
