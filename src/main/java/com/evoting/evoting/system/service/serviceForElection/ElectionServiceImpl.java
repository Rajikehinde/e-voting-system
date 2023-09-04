package com.evoting.evoting.system.service.serviceForElection;

import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.domain.enmPackage.VoteCategory;
import com.evoting.evoting.system.dto.request.ElectionRequest;
import com.evoting.evoting.system.dto.response.Response;
import com.evoting.evoting.system.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;

@Service
public class ElectionServiceImpl implements ElectionService {
    @Autowired
    private ElectionRepository electionRepository;

    @Override
    public boolean Time(ElectionRequest electionRequest) {
        Election time = Election.builder()
                .electionName(electionRequest.getElectionName())
                .electionDate(electionRequest.getElectionDate())
                .electionTimeStart(electionRequest.getElectionTimeStart())
                .electionTimeOut(electionRequest.getElectionTimeOut())
                .build();

        Election savedTime = electionRepository.save(time);
        return true;

    }

//    @Override
//    public boolean startingTime(ElectionRequest electionRequest) {
//        Election election = electionRepository.findById(electionRequest.getElectionId()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
//        LocalTime currentTime = LocalTime.now();
//
//        if (currentTime.isBefore(election.getElectionTimeStart())){
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean endingTime(ElectionRequest electionRequest) {
//        Election election = electionRepository.findById(electionRequest.getElectionId()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
//        LocalTime currentTime = Time.valueOf("17:00:00").toLocalTime();
//
////        if (election.getStartTime().isAfter(currentTime)){
////            System.out.println("Election has ended");
////        }
//        return true;
//    }
}