package parser;

import scanner.MathMethod;
import scanner.Token_Type;

/**
 * Created by KongFanyang on 2016/12/4.
 */
public class ExprNode {
    public Token_Type opCode;
    public ExprNode left, right; //二元运算
    public ExprNode child;
    public MathMethod method;    // 函数调用

    public double caseConst;     // 常数， 绑定右值
    public double caseParmPtr;   // 参数T， 绑定左值
}
