package com.evoting.evoting.system.service.serviceForElection;

import com.evoting.evoting.system.domain.Election;
import com.evoting.evoting.system.dto.request.ElectionRequest;
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
    public Void startingTime(ElectionRequest electionRequest) {
        Election election = electionRepository.findById(electionRequest.getElectionId()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        LocalTime currentTime = Time.valueOf("09:00:00").toLocalTime();

        if (election.getStartTime().isBefore(currentTime)){
            System.out.println("Election hasn't started yet");
        }
        return null;
    }

    @Override
    public Void endingTime(ElectionRequest electionRequest) {
        Election election = electionRepository.findById(electionRequest.getElectionId()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        LocalTime currentTime = Time.valueOf("17:00:00").toLocalTime();

        if (election.getStartTime().isBefore(currentTime)){
            System.out.println("Election has ended");
        }
        return null;
    }
}