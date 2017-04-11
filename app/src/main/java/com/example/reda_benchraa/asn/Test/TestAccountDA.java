package com.example.reda_benchraa.asn.Test;

import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.dataAccess.AccountDA;

import java.sql.SQLException;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class TestAccountDA {
    public static void main(String[] Args) throws SQLException{
        //create account
        Account a = new Account();
        a.setFirstName("rabab");
        a.setSecondName("chahboune");
        a.setAbout("hello");
        a.setEmail("rabab.chahboune@gmail.com");
        a.setShowEmail(true);
        a.setProfilePicture(null);
        AccountDA.insertAccount(a);


        //find account

        Account b = AccountDA.findAccount(2);
        System.out.println(b);


        //update account
        b.setShowEmail(false);
        AccountDA.updateAccount(b);

        //delete account
        AccountDA.deleteAccount(b);

    }
}
