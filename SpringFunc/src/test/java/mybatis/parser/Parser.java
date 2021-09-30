package mybatis.parser;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.ognl.ParseException;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.scripting.xmltags.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Slf4j
public class Parser {
    /**
     * maven 的重要性。
     * maven 删除后重新执行。
     */
    static GenericTokenParser parser =
            new GenericTokenParser("${", "}", new VariableTokenHandler(new HashMap<String, String>() {
                {
                    put("first_name", "James");
                    put("initial", "T");
                    put("last_name", "Kirk");
                    put("var{with}brace", "Hiya");
                    put("", "");
                }
            }));
    static GenericTokenParser parser2 =
            new GenericTokenParser("#{", "}", new VariableTokenHandler(new HashMap<String, String>() {
                {
                    put("first_name", "James");
                    put("initial", "T");
                    put("last_name", "Kirk");
                    put("var{with}brace", "Hiya");
                    put("", "");
                }
            }));

    private MixedSqlNode mixedContents(SqlNode... contents) {
        return new MixedSqlNode(Arrays.asList(contents));
    }

    @Test
    public void concat() {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

        log.info("current classLoader:{},systemClassLoader:{}", this.getClass().getClassLoader(), systemClassLoader);

        SQL sqlWithSelect = new SQL() {{
            this.SELECT("*");
            this.FROM("pg_settings");
            WHERE("NAME ~* 'connection'");
            ORDER_BY("name desc");
        }};
        log.info("{}", sqlWithSelect.toString());
        // insert into a values();
        SQL sqlWithInsert = new SQL() {{
            INSERT_INTO("a").VALUES("col,col2", "val,val2");
        }};
        log.info("{}", sqlWithInsert.toString());

    }


    private Configuration getConfiguration() throws IOException {
        final String resource = "MapperConfig.xml";
        ClassLoader classLoader = this.getClass().getClassLoader();
        final Reader reader = Resources.getResourceAsReader(classLoader, resource);
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        Configuration configuration = sqlMapper.getConfiguration();
        return configuration;
    }

    public DynamicSqlSource createDynamicSqlSource(SqlNode... contents) throws IOException, SQLException {
        Configuration configuration = getConfiguration();
        MixedSqlNode sqlNode = mixedContents(contents);
        return new DynamicSqlSource(configuration, sqlNode);
    }

    @org.junit.Test
    public void loadfromAnnotation() throws IOException, SQLException {

    }


    @org.junit.Test
    public void loadfromScriptFile() throws IOException, SQLException {
        Configuration configuration = getConfiguration();
        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, "<script>\n" +
                "    select * from users where userId=#{userId};\n" +
                "</script>", null);

        BoundSql boundSql = sqlSource.getBoundSql(null);
        String sql = boundSql.getSql();
        log.info("sql: {}", sql);
    }

    public static enum ItemType {
        A, B;

        public String getValue() {
            return this.ordinal() == 0 ? "A" : "B";
        }
    }
    /**
     * 1. 初始化logSystem
     */
    @Test
    public void parseSqlNodeWithinOgnl() throws IOException, SQLException {

        String value = ItemType.A.getValue();
        // @mybatis.parser.Parser$ItemType@A.getValue()
        VarDeclSqlNode varDeclSqlNode = new VarDeclSqlNode("a","@mybatis.parser.Parser$ItemType@A.getValue");
        String sql = "select * from users where item_type= #{a}";
        TextSqlNode textSqlNode = new TextSqlNode(sql);

        DynamicSqlSource dynamicSqlSource = createDynamicSqlSource(textSqlNode);
        HashMap<String, Object> paramterMap = new HashMap<String, Object>() {
            {
                this.put("userId", "111");
                this.put("list", Lists.newArrayList("1", "2"));
            }
        };
        String sql1 = dynamicSqlSource.getBoundSql(paramterMap).getSql();
        dynamicSqlSource.getBoundSql(paramterMap);
        log.info("{},finally sql is {}", value,sql1);
        // get value
    }

    @Test
    public void parseTrimSqlNode() throws IOException, SQLException {
        Configuration configuration = getConfiguration();
        String sql = "select * from users where ";
        TextSqlNode textSqlNode = new TextSqlNode(sql);
        // IfSqlNode

        TextSqlNode toBeDeletedSql = new TextSqlNode("  userId=#{userId} or");
        TextSqlNode toBeDeletedSql1 = new TextSqlNode(" userId=#{userId} and");
        IfSqlNode ifSqlNode = new IfSqlNode(toBeDeletedSql, "list[0]==2");


        /**
         * deleted    (and  | or )
         */
        TrimSqlNode trimAndIfSqlNode = new TrimSqlNode(configuration, ifSqlNode,
                null, null, null, "and | or");
        TrimSqlNode trimSqlNode1 = new TrimSqlNode(configuration, toBeDeletedSql1,
                null, null, null, "and | or");
        /**
         *
         * select * from users where
         * <trim>
         *   userId=#{userId}
         * </trim>
         */
        DynamicSqlSource dynamicSqlSource = createDynamicSqlSource(textSqlNode, trimAndIfSqlNode, trimSqlNode1);
        HashMap<String, Object> paramterMap = new HashMap<String, Object>() {
            {
                this.put("userId", "111");
                this.put("list", Lists.newArrayList("1", "2"));
            }
        };
        String sql1 = dynamicSqlSource.getBoundSql(paramterMap).getSql();
        log.info("finally sql is {}", sql1);
        String format = String.format("%s", sql1);
        log.info("another sql output:,{}", format);
        String expectedSql = "select * from users where   userId=?";
        Assert.isTrue(format.equals(expectedSql) && sql1.equals(expectedSql));
        // 1. jsqlParser  parse sql ;
        // 2. ognl  -> execute   expression

    }

    /**
     *
     */
    @Test
    public void parseDynamicSqlSource() throws IOException, SQLException {
        String expected = "select * from users where userId = ?";
        TextSqlNode textSqlNode = new TextSqlNode("select * from users where userId = #{userId,javaType=java.lang.Integer,jdbcType=INTEGER}");
        DynamicSqlSource dynamicSqlSource = createDynamicSqlSource(textSqlNode);
        HashMap map = new HashMap<String, String>() {{
            put("userId", "111");
        }};
        BoundSql boundSql = dynamicSqlSource.getBoundSql(map);
        String sql = boundSql.getSql();
        log.info("sql: {}", sql);
        Assert.isTrue(sql.equals(expected));
        Assert.isTrue(((Map) boundSql.getAdditionalParameter(DynamicContext.PARAMETER_OBJECT_KEY)).get("userId").equals("111"));

    }


    @Test
    public void parse() throws ParseException {
        /**
         * ${first_name}
         */
        assertEquals(parser.parse("${first_name}"), "James");
        assertEquals(parser.parse("'${first_name}'||hel"), "'James'||hel");
        assertEquals(parser.parse("'${var{with}brace}'||hel"), "'nullbrace}'||hel");
        assertEquals(parser2.parse("'#{first_name}'||hel"), "'James'||hel");
        assertEquals(parser2.parse("#{first_name}||hel"), "James||hel");
        String finalOutput = parser2.parse("#{first_name}||hel" +
                "<choose>" +
                "   <when test=\"first_name=='James'\">yes he is</when>" +
                "   <otherwise>no he doesn't</otherwise>" +
                "</choose> ");
        log.info("======={}", finalOutput);
        // mysql parse  ognl -> xmltags ->


    }
}
