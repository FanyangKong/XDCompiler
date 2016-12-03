package literial;

import scanner.Token;
import scanner.Token_Type;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public class Constants {
    public static Token[] TokenTable = new Token[]{
            new Token(Token_Type.CONST_ID, "PI", 3.1415926, null),
            new Token(Token_Type.CONST_ID, "E", 2.71828, null),
            new Token(Token_Type.T, "T", 0, null),
            new Token(Token_Type.FUNC, "SIN", 0, Math::sin),
            new Token(Token_Type.FUNC, "COS", 0, Math::cos),
            new Token(Token_Type.FUNC, "TAN", 0, Math::tan),
            new Token(Token_Type.FUNC, "LN", 0, Math::log10),
            new Token(Token_Type.FUNC, "EXP", 0, Math::exp),
            new Token(Token_Type.FUNC, "SQRT", 0, Math::sqrt),
            new Token(Token_Type.ORIGIN, "ORIGIN", 0, null),
            new Token(Token_Type.SCALE, "SCALE", 0,null),
            new Token(Token_Type.ROT, "ROT", 0, null),
            new Token(Token_Type.IS, "IS", 0, null),
            new Token(Token_Type.FOR, "FOR", 0, null),
            new Token(Token_Type.FROM, "FROM", 0, null),
            new Token(Token_Type.TO, "TO", 0, null),
            new Token(Token_Type.STEP, "STEP", 0, null),
            new Token(Token_Type.DRAW, "DRAW", 0, null),

    };

}
