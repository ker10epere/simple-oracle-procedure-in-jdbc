package com.dodaily.oracleprocedure.procedures;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

@NoArgsConstructor
@ToString
public class SqlFood extends Food implements SQLData {
    public SqlFood(String name, Integer age) {
        super(name, age);
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return "FOOD";
    }

    public static String typeName() throws SQLException {
        return "FOOD";
    }

    @Override
    public void readSQL(SQLInput input, String typeName) throws SQLException {
        setName(input.readString());
        setAge(input.readInt());
    }

    @Override
    public void writeSQL(SQLOutput output) throws SQLException {
        output.writeString(getName());
        output.writeInt(getAge());
    }
}
