package scanner;

import util.Constants;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public class Scanner {
    private static Scanner scanner;
    private List<Token> tokens;
    private String content;
    private int index = 0;
    private String tokenBuffer = "";
    private File file;
    private BufferedReader bf;
    private int lineNo = 0;

    private Scanner(String path) {
        tokens = new LinkedList<>();
        // 读取文件
        try {
            file = new File(path);
            bf = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void init(String path) {
        if (scanner == null) {
            scanner = new Scanner(path);
        }
        scanner.scan();
    }


    public static List<Token> getToken() {
        return scanner.tokens;
    }

    public char getChar() {
        if (index < content.length()) {
            return content.charAt(index++);
        } else {
            index++;
            return '\0';
        }
    }


    public void backChar() {
        /**
         * todo 处理index下标越界问题
         */
        index--;
    }

    public static void printTokens() {
        List<Token> list = getToken();
        System.out.printf("      记号类别           字符串        常数值    函数指针\n");
        System.out.printf("——————————————————————————————————————\n");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%10s(%2d)    %12s    %12f    ", list.get(i).type.name(), list.get(i).type.ordinal() + 1, list.get(i).lexeme, list.get(i).value);
            System.out.println(list.get(i).method);
        }
        System.out.printf("—————————————行号 " + scanner.lineNo + "—————————————————————————\n");

    }

    /**
     * 测试file
     *
     * @param args
     */
    public static void main(String[] args) {
        String str;
        try {

            File file = new File("src/Hello");
            BufferedReader b = new BufferedReader(new FileReader(file));
            while ((str = b.readLine()) != null)
                System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scan() {
        char c;
        outer:
        while ((content = readFile()) != null) {//只读取一行文件
            emptyTokenString();
            while ((c = getChar()) != '\0') {
                Token token = new Token(Token_Type.ERRTOKEN, "", 0, null);
                token.setLinNo(lineNo);
                if (isSpace(c)) {
                    continue;
                } else if (isAlpha(c)) {
                    token.lexeme = tokenBuffer;
                    //查找TokenTable中是否有该关键字
                    for (int i = 0; i < Constants.TokenTable.length; i++) {
                        if (tokenBuffer.toUpperCase().equals(Constants.TokenTable[i].lexeme)) {//tokenBuffer的大写形式
                            token.value = Constants.TokenTable[i].value;
                            token.type = Constants.TokenTable[i].type;
                            token.method = Constants.TokenTable[i].method;
                            break;
                        }
                    }

                    //如果TokenTable中查不到该关键字，则为错误关键字
                    if (token.type == Token_Type.ERRTOKEN) {
                        token.lexeme = tokenBuffer;
                        token.value = lineNo;
                    }

                    tokens.add(token);
                } else if (isDigit(c)) {
                    token.lexeme = tokenBuffer;
                    token.type = Token_Type.CONST_ID;
                    token.value = Double.valueOf(tokenBuffer);
                    tokens.add(token);
                } else {
                    token.lexeme = c + "";
                    switch (c) {
                        case ';':
                            token.type = Token_Type.SEMICO;
                            tokens.add(token);
                            break;
                        case '(':
                            token.type = Token_Type.L_BRACKET;
                            tokens.add(token);
                            break;
                        case ')':
                            token.type = Token_Type.R_BRACKET;
                            tokens.add(token);
                            break;
                        case ',':
                            token.type = Token_Type.COMMA;
                            tokens.add(token);
                            break;
                        case '+':
                            token.type = Token_Type.PLUS;
                            tokens.add(token);
                            break;
                        case '-':
                            if (getChar() == '-') {
                                // --代表注释，检测到注释直接跳过剩下的内容进入下一行
                                resetIndex();
                                continue outer;
                            }
                            backChar();
                            token.type = Token_Type.MINUS;
                            tokens.add(token);
                            break;
                        case '*':
                            if ((c = getChar()) == '*') {
                                token.type = Token_Type.POWER;
                                token.lexeme += c;
                            } else {
                                backChar();
                                token.type = Token_Type.MUL;
                            }
                            tokens.add(token);
                            break;
                        case '/':
                            if (getChar() == '/') {
                                // //代表注释，检测到注释直接跳过剩下的内容进入下一行
                                resetIndex();
                                continue outer;
                            }
                            backChar();
                            token.type = Token_Type.DIV;
                            tokens.add(token);
                            break;
                        default:
                            tokens.add(token);
                    }
                }

                emptyTokenString();
            }
            resetIndex();
        }

        tokens.add(new Token(Token_Type.NONTOKEN, "", 0, null));
    }


    private Pattern letAndDig = Pattern.compile("[0-9a-zA-Z]");    //letter|digit
    private Pattern let = Pattern.compile("[a-zA-Z]");    //letter
    private Pattern dig = Pattern.compile("[0-9]");    //digit


    /**
     * 从c开始向后读取字符，判断出一个完整的单词，匹配DFA
     *
     * @param c
     * @return
     */
    private boolean isAlpha(char c) {
        if (let.matcher(c + "").matches()) {
            addCharToTokenString(c);
            while (letAndDig.matcher((c = getChar()) + "").matches()) {
                addCharToTokenString(c);
            }
            backChar();
            return true;
        }
        return false;
    }

    /**
     * 从c开始向后读取字符，判断出一个完整的数字，匹配DFA
     *
     * @param c
     * @return
     */
    private boolean isDigit(char c) {
        if (dig.matcher(c + "").matches()) {
            addCharToTokenString(c);
            while (dig.matcher((c = getChar()) + "").matches()) {
                addCharToTokenString(c);
            }
            backChar();
            c = getChar();
            if (c == '.') {
                addCharToTokenString(c);
                while (dig.matcher((c = getChar()) + "").matches()) {
                    addCharToTokenString(c);
                }
            }
            backChar();
            return true;
        }

        return false;
    }

    private boolean isSpace(char c) {

        return c == ' ' || c == '\n' || c == '\t';
    }



    /**
     * 每调用一次读取文件中一行字符
     *
     * @return 文件中的下一行字符
     */
    private String readFile() {
        String s;

        try {
            if ((s = bf.readLine()) != null) {
                lineNo++;
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void close() {
        scanner.stop();
    }

    private void stop() {
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCharToTokenString(char c) {
        tokenBuffer += c;
    }

    private void emptyTokenString() {
        tokenBuffer = "";
    }

    private void resetIndex() {
        index = 0;
    }
}

