package com.dodaily.oracleprocedure;

import com.dodaily.oracleprocedure.procedures.Food;
import com.dodaily.oracleprocedure.procedures.FoodMapper;
import com.dodaily.oracleprocedure.procedures.SqlFood;
import com.dodaily.oracleprocedure.procedures.SqlFoodArray;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Runner implements CommandLineRunner {
    final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {

        /*
        CREATE OR REPLACE TYPE OISS.food AS OBJECT
        (
            name VARCHAR2 (100),
            age NUMBER
        );

        CREATE OR REPLACE TYPE OISS.foodlist IS TABLE OF food;

        CREATE OR REPLACE PROCEDURE OISS.ARR_INFOOD3(foods IN FOODLIST, foods2 OUT FOODLIST)
        IS
        BEGIN
         foods2:= foods;
        END ARR_INFOOD3;
         */


        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("ARR_INFOOD3")
                .declareParameters(
                        new SqlParameter("foods", OracleTypes.ARRAY, "FOODLIST"),
                        new SqlOutParameter("foods2", OracleTypes.ARRAY, "FOODLIST" )
                );

        // ORACLE TYPE IS CALLED STRUCT IN JDBC
        // MAP STRUCT TYPE TO JAVA COMPATIBLE TYPE
        // SqlFood EXTENDS THE ACTUAL Food ENTITY
        SqlFood kikyam = new SqlFood("Kikyam", 100);

        // INSERTING ORACLE ARRAY INPUTS REQUIRES SPECIAL DATA TYPES
        // MUST MAP PLAIN JAVA LIST TO ORACLE COMPATIBLE ARRAY
        SqlFoodArray arrayInputs = new SqlFoodArray(Collections.singletonList(kikyam));
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("foods", arrayInputs);

        Map<String, Object> out = simpleJdbcCall.execute(source);
        ARRAY foods2 = (ARRAY) out.get("foods2");

        List<Struct> collect = Arrays.stream(foods2.getOracleArray()).map(o -> (Struct) o).collect(Collectors.toList());
        collect.forEach(o-> {
            try {
                // FoodMapper IS JUST A MAPPER OF STRUCT TO Food PLAIN JAVA OBJECT
                Food food = FoodMapper.mapRow(o);
                System.out.println(food);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

//
//    @Override
//    public void run(String... args) throws Exception {
//        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("ARR_INFOOD2")
//                .declareParameters(
//                        new SqlParameter("foods", OracleTypes.ARRAY, "FOODLIST"),
//                        new SqlOutParameter("name", OracleTypes.VARCHAR)
//                );
//        MapSqlParameterSource source = new MapSqlParameterSource().addValue("foods",new SqlFoodArray(Collections.singletonList(new SqlFood("Kikyam", 100))) );
//
//        Map<String, Object> out = simpleJdbcCall.execute(source);
//        System.out.println(out.values().stream().toList());
//    }



//    @Override
//    public void run(String... args) throws Exception {
//        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource).withProcedureName("SP_IN_RPS")
//                .declareParameters(
//                        new SqlParameter("food", OracleTypes.STRUCT, "FOOD"),
//                        new SqlOutParameter("foodout", OracleTypes.STRUCT, "FOOD")
//                );
//        MapSqlParameterSource source = new MapSqlParameterSource().addValue("food", new SqlFood("Kikyam", 100));
//
//        Map<String, Object> out = simpleJdbcCall.execute(source);
//        System.out.println(FoodMapper.mapRow(out.get("foodout")));
//    }
}