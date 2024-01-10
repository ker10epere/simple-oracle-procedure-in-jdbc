package com.dodaily.oracleprocedure.procedures;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



public class SqlFoodArray extends AbstractSqlTypeValue {
    private final List<SqlFood> foods;

    public SqlFoodArray(List<SqlFood> foods) {
        this.foods = foods;
    }

    @Override
    protected Object createTypeValue(Connection con, int sqlType, String typeName) throws SQLException {
        oracle.jdbc.OracleConnection wrappedConnection = con.unwrap(oracle.jdbc.OracleConnection.class);
        con = wrappedConnection;
        ArrayDescriptor desc = new ArrayDescriptor(typeName, con);
        return new ARRAY(desc, con, foods.toArray());
    }
}
