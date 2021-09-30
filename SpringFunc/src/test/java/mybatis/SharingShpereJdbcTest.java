package mybatis;

import io.shardingsphere.core.parsing.lexer.LexerEngine;
import io.shardingsphere.core.parsing.lexer.dialect.mysql.MySQLLexer;
import io.shardingsphere.core.parsing.parser.clause.InsertValuesClauseParser;
import io.shardingsphere.core.parsing.parser.dialect.mysql.clause.MySQLInsertValuesClauseParser;
import io.shardingsphere.jdbc.spring.datasource.SpringShardingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.mock;
@Slf4j
public class SharingShpereJdbcTest {
    @Test
    public void test2(){
        String sql = "insert tables(a,b) values \n";
        LexerEngine lexerEngine = new LexerEngine(new MySQLLexer(sql));
        lexerEngine.nextToken();

        String input = lexerEngine.getInput();
        log.info("result,{}",input);
//        InsertValuesClauseParser.class;

    }
    @Test
    public void test1() throws SQLException {
        SpringShardingDataSource mock = mock(SpringShardingDataSource.class);
        String sql = "insert tables(a,b) values \n" +
                "(1,2)";
        Statement statement = mock.getConnection().createStatement();
        boolean execute = statement.execute(sql);



    }
}
