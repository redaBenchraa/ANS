package com.example.reda_benchraa.asn.dataAccess;

import com.example.reda_benchraa.asn.DB.DataAccess;
import com.example.reda_benchraa.asn.Model.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */
//I am not sure a picture should be added this way but as long as I did not create a utility to do it...
public class AccountDA {

    private final static String tableName = "account";

    public static Account findAccount(int id)throws SQLException {
        DataAccess.setDbname("ASN");
        Connection con = DataAccess.getInstance().getConnection();
        String sql = "SELECT * FROM " + tableName + " where id = ?";
        ResultSet result = DataAccess.select(con, sql, id);
        if (result.next()){
            Account Account = map(result);
            //UtilHelper.close(result);
            //UtilHelper.close(con);
            return Account;
        }
        else{
            //UtilHelper.close(result);
            //UtilHelper.close(con);
            return null;
        }
    }

    public static int updateAccount(Account a) throws SQLException {
        DataAccess.setDbname("ASN");
        Connection con = DataAccess.getInstance().getConnection();
        String sql = "UPDATE "+ tableName + " SET FName = ?,LName = ?,Email = ?, About=?,showEmail = ?,Picture = ? where id=? ";
        int i = DataAccess.executeSQL(con, sql,
                a.getFirstName(),
                a.getSecondName(),
                a.getEmail(),
                a.getAbout(),
                a.isShowEmail(),
                a.getProfilePicture(),
                a.getId());
        //UtilHelper.close(con);
        return i;
    }

    public static int insertAccount(Account a) throws SQLException {
        DataAccess.setDbname("ASN");
        Connection con = DataAccess.getInstance().getConnection();
        String sql = "INSERT INTO " + tableName
                + " (FName, LName,Email,About,showEmail,Picture) "
                + "VALUES ( ?, ? , ?, ? , ?, ? )";
        int i= DataAccess.executeSQL(con, sql,
                a.getFirstName(),
                a.getSecondName(),
                a.getEmail(),
                a.getAbout(),
                a.isShowEmail(),
                a.getProfilePicture()
        );
        //UtilHelper.close(con);
        return i;
    }

    public static int deleteAccount(Account a) throws SQLException {
        DataAccess.setDbname("ASN");
        Connection con = DataAccess.getInstance().getConnection();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        return DataAccess.executeSQL(con, sql, a.getId());
    }

    private static Account map(ResultSet resultSet) throws SQLException {
        Account a = new Account();
        a.setId(resultSet.getInt("Id"));
        a.setFirstName(resultSet.getString("Fname"));
        a.setSecondName(resultSet.getString("LName"));
        a.setEmail(resultSet.getString("Email"));
        a.setShowEmail(resultSet.getBoolean("showEmail"));
        a.setAbout(resultSet.getString("About"));
        a.setProfilePicture(resultSet.getBlob("Picture"));
        a.setCreatedGroups(GroupDA.getOwnedGroupList(a));
        //other fields till we create the other DA models

        return a;
    }
}
