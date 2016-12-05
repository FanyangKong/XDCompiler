package util;

import parser.ExprNode;
import scanner.Token_Type;

/**
 * Created by KongFanyang on 2016/12/5.
 */
public class Log {

    private static boolean logon = false;

    public static void log(boolean b) {
        logon = b;
    }

    public static void enter(String name) {
        if (logon) {
            System.out.println("enter in " + name);
        }
    }

    public static void back(String name) {
        if (logon) {
            System.out.println("back from " + name);
        }
    }

    public static void call_match(String name) {
        if (logon) {
            System.out.println("match token " + name);
        }
    }

    public static void tree_trace(ExprNode root, int indent) {
        if (logon) {
            int temp;
            for (temp = 1; temp <= indent; temp++)
                System.out.print("\t");
            switch (root.opCode) {
                case PLUS:
                    System.out.println("+");
                    break;
                case MINUS:
                    System.out.println("-");
                    break;
                case MUL:
                    System.out.println("*");
                    break;
                case DIV:
                    System.out.println("/");
                    break;
                case POWER:
                    System.out.println("**");
                    break;
                case FUNC:
                    System.out.println(root.method);
                    break;
                case CONST_ID:
                    System.out.println(root.caseConst + "");
                    break;
                case T:
                    System.out.println("T");
                    break;
                default:
                    System.out.println("Error Tree Node");
                    // todo exit(0)
            }

            if (root.opCode == Token_Type.CONST_ID || root.opCode == Token_Type.T) {
                return;
            }
            if (root.opCode == Token_Type.FUNC) {
                tree_trace(root.child, indent + 1);
            } else {
                tree_trace(root.left, indent + 1);
                tree_trace(root.right, indent + 1);
            }
        }
    }

}
