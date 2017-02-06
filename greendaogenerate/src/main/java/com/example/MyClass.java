package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {
    public static void main(String args[]){
        Schema schema=new Schema(1,"com.example");
        Entity city=schema.addEntity("City");


        city.addStringProperty("cityZh");

        city.addStringProperty("provinceZh");

        city.addStringProperty("leaderZh");

        try {
            new DaoGenerator().generateAll(schema,"app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
