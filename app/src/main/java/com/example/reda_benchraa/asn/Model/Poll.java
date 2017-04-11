package com.example.reda_benchraa.asn.Model;

import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Poll {
    LinkedList<PollOption> pollOptions = new LinkedList<>();
    PollPrivacy pollPrivacy;

    class PollOption {
        String text;
        LinkedList<Account> voters;
    }

    enum PollPrivacy { VISIBLE_VOTERS, ANONYMOUS_VOTERS }

    enum PollType { ONLY_ONE_CHOICE, MANY_CHOICES}

    public Poll() {
    }

    public LinkedList<PollOption> getPollOptions() {
        return pollOptions;
    }

    public void setPollOptions(LinkedList<PollOption> pollOptions) {
        this.pollOptions = pollOptions;
    }

    public PollPrivacy getPollPrivacy() {
        return pollPrivacy;
    }

    public void setPollPrivacy(PollPrivacy pollPrivacy) {
        this.pollPrivacy = pollPrivacy;
    }
}
