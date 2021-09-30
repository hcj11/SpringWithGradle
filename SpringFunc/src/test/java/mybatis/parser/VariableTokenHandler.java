package mybatis.parser;

import org.apache.ibatis.parsing.TokenHandler;

import java.util.HashMap;

public class VariableTokenHandler implements TokenHandler {

    static HashMap<String, String> map = null;

    public VariableTokenHandler(HashMap<String, String> hashMap) {
        map = hashMap;
    }

    @Override
    public String handleToken(String content) {
        return map.get(content);
    }
}
