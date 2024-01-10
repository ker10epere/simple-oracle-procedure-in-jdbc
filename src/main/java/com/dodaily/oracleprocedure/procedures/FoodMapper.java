package com.dodaily.oracleprocedure.procedures;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Struct;

public class FoodMapper {

    public static Food mapRow(Object object) throws SQLException {
        Object[] attributes = ((Struct) object).getAttributes();

        return new Food((String) attributes[0], ((BigDecimal) attributes[1]).intValue());
    }
}
