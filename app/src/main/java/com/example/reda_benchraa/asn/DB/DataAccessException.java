package com.example.reda_benchraa.asn.DB;

/**
 * Created by Rabab Chahboune on 4/7/2017.
 */

public class DataAccessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
