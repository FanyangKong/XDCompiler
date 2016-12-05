package util;

import parser.ExprNode;

/**
 * Created by KongFanyang on 2016/12/5.
 */
public class Calculate {
    // todo 此处第二个参数为循环过程中的原值，对Java中无指针的折中办法，应该想办法去掉
    // 重载此函数的原因是不想修改过多代码
    public static double getExprValue(ExprNode root, double origin) {
        if (root == null)
            return 0;
        switch (root.opCode) {
            case PLUS:
                return getExprValue(root.left, origin) + getExprValue(root.right, origin);
            case MINUS:
                return getExprValue(root.left, origin) - getExprValue(root.right, origin);
            case MUL:
                return getExprValue(root.left, origin) * getExprValue(root.right, origin);
            case DIV:
                return getExprValue(root.left, origin) / getExprValue(root.right, origin);
            case POWER:
                return Math.pow(getExprValue(root.left, origin), getExprValue(root.right, origin));
            case FUNC:
                return root.method(getExprValue(root.child, origin));
            case CONST_ID:
                return root.caseConst;
            case T:
                return origin;//root.caseParmPtr
            default:
                return 0;
        }
    }

    public static double getExprValue(ExprNode root) {
        if (root == null)
            return 0;
        switch (root.opCode) {
            case PLUS:
                return getExprValue(root.left) + getExprValue(root.right);
            case MINUS:
                return getExprValue(root.left) - getExprValue(root.right);
            case MUL:
                return getExprValue(root.left) * getExprValue(root.right);
            case DIV:
                return getExprValue(root.left) / getExprValue(root.right);
            case POWER:
                return Math.pow(getExprValue(root.left), getExprValue(root.right));
            case FUNC:
                return root.method(getExprValue(root.child));
            case CONST_ID:
                return root.caseConst;
            case T:
                return root.caseParmPtr;//root.caseParmPtr
            default:
                return 0;
        }
    }



}
