package com.example.reda_benchraa.asn.Model;

import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class Poll {
    LinkedList<PollOption> pollOptions;
    PollPrivacy pollPrivacy;

    class PollOption {
        String text;
        LinkedList<Account> voters;
    }

    enum PollPrivacy { VISIBLE_VOTERS, ANONYMOUS_VOTERS }

    enum PollType { ONLY_ONE_CHOICE, MANY_CHOICES}

}
