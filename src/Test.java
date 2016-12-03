import scanner.Scanner;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public class Test {
    public static void main(String[] args) {

        String path = "src/Hello";

        Scanner.init(path);

        Scanner.printTokens();

        Scanner.close();


    }
}
