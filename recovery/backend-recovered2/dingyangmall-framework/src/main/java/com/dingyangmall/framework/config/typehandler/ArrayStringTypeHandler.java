/*
 * Decompiled with CFR.
 */
package com.dingyangmall.framework.config.typehandler;

import cn.hutool.json.JSONUtil;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value={String[].class})
@MappedJdbcTypes(value={JdbcType.VARCHAR})
public class ArrayStringTypeHandler
extends BaseTypeHandler<String[]> {
    private static String[] l = new String[0];

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JSONUtil.parseArray(rs.getString(columnName)).toArray(l);
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JSONUtil.parseArray(rs.getString(columnIndex)).toArray(l);
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JSONUtil.parseArray(cs.getString(columnIndex)).toArray(l);
    }
}

