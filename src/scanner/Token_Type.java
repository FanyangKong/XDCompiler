package scanner;

/**
 * Created by KongFanyang on 2016/11/28.
 */
public enum Token_Type {                // 记号类别
    ORIGIN, SCALE, ROT, IS, TO, STEP, DRAW, FOR, FROM,             // 保留字
    T,                                                             // 参数
    SEMICO, L_BRACKET, R_BRACKET, COMMA,                            // 分隔符 ; ( ) ,
    PLUS, MINUS, MUL, DIV, POWER,                                   // 运算符 + - * / **
    FUNC,                                                           // 函数
    CONST_ID,                                                       // 常数
    NONTOKEN,                                                       // 空记号
    ERRTOKEN                                                        // 出错记号
}
