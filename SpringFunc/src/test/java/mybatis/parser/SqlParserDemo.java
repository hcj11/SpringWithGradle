package mybatis.parser;

import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParser;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
public class SqlParserDemo {

    @Test
    public void try1WithNetSF() throws FileNotFoundException, ParseException {
        // load sql Snippet;

        CCJSqlParser ccjSqlParser = new CCJSqlParser(new FileInputStream(""));
        ccjSqlParser.FromItem();
        ccjSqlParser.AllTableColumns().getTable().getWholeTableName();
    }

    @Test
    public void IdentifySqlWithDurid() {
        String sql = "insert into user(user_id,user_name,birthday) values(UUID(),#{userName},CURRENT_DATE),(UUID(),#{userName},CURRENT_DATE);";
        SQLParser sqlParser = new SQLParser(sql);
        log.info("=============sqlParser:{}", sqlParser.toString());
        String dbType = sqlParser.getDbType();
        Lexer lexer = sqlParser.getLexer();
        StringBuffer stringBuffer = new StringBuffer(1024);

        while (!lexer.isEOF()) {
            lexer.nextToken();
        }


    }
}
