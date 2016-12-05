import parser.Parser;
import scanner.Scanner;
import util.Log;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public class Test {
    public static void main(String[] args) {

        Log.log(true);

        String path = "src/Hello";

        //Scanner.init(path);

        //Scanner.printTokens();

        //Scanner.close();

        Parser parser = Parser.newInstance(path);
        Scanner.printTokens();
        parser.program();

    }
}
