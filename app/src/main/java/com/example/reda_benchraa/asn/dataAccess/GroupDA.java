package com.example.reda_benchraa.asn.dataAccess;

import com.example.reda_benchraa.asn.DB.DataAccess;
import com.example.reda_benchraa.asn.Model.Account;
import com.example.reda_benchraa.asn.Model.Group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class GroupDA {
    private final static String tableName = "_group";
    public static LinkedList<Group> getOwnedGroupList(Account a)throws SQLException {
        DataAccess.setDbname("ASN");
        Connection con = DataAccess.getInstance().getConnection();
        String sql = "SELECT * FROM " + tableName +" where ID_Account=?" ;
        ResultSet result = DataAccess.select(con, sql,a.getId());
        LinkedList<Group> list = new LinkedList<>();
        while (result.next()) {
            list.add(map(result));
        }
        //UtilHelper.close(con);
        //UtilHelper.close(result);
        return list;
    }

    private static Group map(ResultSet resultSet) throws SQLException {
        Group g = new Group();
        g.setId(resultSet.getInt("ID"));
        g.setName(resultSet.getString("name"));
        g.setCreationDate(resultSet.getDate("creationDate"));
        g.setAbout(resultSet.getString("About"));
        return g;
    }

}
