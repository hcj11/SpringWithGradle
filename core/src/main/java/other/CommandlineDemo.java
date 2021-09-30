package other;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.nio.file.Files;

/**
 * √¸¡Ó––Ω‚Œˆ
 */
public class CommandlineDemo {
    public static void demo1() throws ParseException {
        // unix Ω‚Œˆ
        BasicParser basicParser = new BasicParser();
        Options options = new Options();
        options.addOption(new Option("","others"));
        String[] str={"1","2"};
        basicParser.parse(options,str);

    }
    public static void main(String[] args) {

    }
}
