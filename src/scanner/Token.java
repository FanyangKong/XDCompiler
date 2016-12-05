package scanner;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public class Token {                // 记号
    public Token_Type type;         // 类型
    public String lexeme;           // 属性，原始输入的字符串
    public double value;            // 属性，常数值
    public MathMethod method;       // 接口+lambda
    public int linNo;

    public Token(Token_Type type, String lexeme, double value, MathMethod method) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = value;
        this.method = method;
    }

    public void setLinNo(int no) {
        this.linNo = no;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Token && this.lexeme.equals(((Token) obj).lexeme);
    }

}
