package org.mon.lottery_system.dao.handler;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.mon.lottery_system.dao.dataobject.Encrypt;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Encrypt.class) //被处理的类型
@MappedJdbcTypes(JdbcType.VARCHAR)  //转换后的JDBC类型
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {

//    密钥
    private final byte[] KEY="123456789abcdefg".getBytes(StandardCharsets.UTF_8);

    /**
     * 获取值
     * @param rs  结果集
     * @param columnIndex  索引名
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        System.out.println("获取值得到的加密内容："+rs.getString(columnIndex));
        return decrypt(rs.getString(columnIndex));
    }

    /**
     * 设置参数，处理参数
     * @param ps  SQL预编译对象
     * @param i   需要赋值的索引位置
     * @param parameter  原本索引位置i需要赋的值
     * @param jdbcType   JDBC的类型
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if(parameter==null||parameter.getValue()==null){
            ps.setString(i,null);
            return ;
        }
//        加密
        System.out.println("加密的内容："+parameter.getValue());
        AES aes= SecureUtil.aes(KEY);
        String str=aes.encryptHex(parameter.getValue());
        ps.setString(i,str);
    }

    /**
     * 获取值
     * @param rs
     *          the rs
     * @param columnName
     *          Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
     *
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        System.out.println("获取值得到的加密内容："+rs.getString(columnName));
        return decrypt(rs.getString(columnName));
    }

    /**
     * 获取值
     * @param cs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        System.out.println("获取值得到的加密内容："+cs.getString(columnIndex));
        return decrypt(cs.getString(columnIndex));
    }

    /**
     * 解密
     * @param str
     * @return
     */
    private Encrypt decrypt(String str){
        if(!StringUtils.hasText(str)){
            return null;
        }
        return new Encrypt(SecureUtil.aes(KEY).decryptStr(str));
    }
}
