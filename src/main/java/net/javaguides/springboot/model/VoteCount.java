package net.javaguides.springboot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class VoteCount {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;
    private String totalVoter;
    private String currentVotecount;

    public String getTotalVoter() {
        return totalVoter;
    }

    public void setTotalVoter(String totalVoter) {
        this.totalVoter = totalVoter;
    }

    public String getCurrentVotecount() {
        return currentVotecount;
    }

    public void setCurrentVotecount(String currentVotecount) {
        this.currentVotecount = currentVotecount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
