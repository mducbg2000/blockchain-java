package repository;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;

public class Database {

    private static final String DBFILE = "Database/Test3.db";
    private static final DB db;

    static {
        db = DBMaker.fileDB(new File(DBFILE))
                .closeOnJvmShutdown()
                .transactionEnable()
                .make();
    }

    public static DB getDB() {
        return db;
    }

}
