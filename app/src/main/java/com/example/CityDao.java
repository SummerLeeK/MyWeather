package com.example;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CITY".
*/
public class CityDao extends AbstractDao<City, Void> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CityZh = new Property(0, String.class, "cityZh", false, "CITY_ZH");
        public final static Property ProvinceZh = new Property(1, String.class, "provinceZh", false, "PROVINCE_ZH");
        public final static Property LeaderZh = new Property(2, String.class, "leaderZh", false, "LEADER_ZH");
    }


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITY\" (" + //
                "\"CITY_ZH\" TEXT," + // 0: cityZh
                "\"PROVINCE_ZH\" TEXT," + // 1: provinceZh
                "\"LEADER_ZH\" TEXT);"); // 2: leaderZh
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, City entity) {
        stmt.clearBindings();
 
        String cityZh = entity.getCityZh();
        if (cityZh != null) {
            stmt.bindString(1, cityZh);
        }
 
        String provinceZh = entity.getProvinceZh();
        if (provinceZh != null) {
            stmt.bindString(2, provinceZh);
        }
 
        String leaderZh = entity.getLeaderZh();
        if (leaderZh != null) {
            stmt.bindString(3, leaderZh);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        String cityZh = entity.getCityZh();
        if (cityZh != null) {
            stmt.bindString(1, cityZh);
        }
 
        String provinceZh = entity.getProvinceZh();
        if (provinceZh != null) {
            stmt.bindString(2, provinceZh);
        }
 
        String leaderZh = entity.getLeaderZh();
        if (leaderZh != null) {
            stmt.bindString(3, leaderZh);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // cityZh
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // provinceZh
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // leaderZh
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setCityZh(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setProvinceZh(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLeaderZh(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(City entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(City entity) {
        return null;
    }

    @Override
    public boolean hasKey(City entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}