package parser;

import graphic.XDGraphics;
import scanner.MathMethod;
import scanner.Scanner;
import scanner.Token;
import scanner.Token_Type;
import util.Calculate;
import util.Log;

import javax.swing.*;
import java.util.List;

/**
 * Created by KongFanyang on 2016/12/4.
 */
public class Parser {
    private static Parser parser;
    private List<Token> tokenList;
    private Token token;
    private static int index;

    private Parser(String path) {
        //调用词法分析器
        startScanner(path);
        fetchToken();
        //以下两个移至main函数显示调用
        //program();
        //drawGraphics();
    }

    private void startScanner(String path) {
        Scanner.init(path);
        tokenList = Scanner.getToken();
        Scanner.close();
    }

    public static Parser newInstance(String path) {
        if (parser == null) {
            parser = new Parser(path);
        }
        return parser;
    }

    public void program() {
        Log.enter("program");
        while (token.type != Token_Type.NONTOKEN) {
            statements();           //进行整行代码的递归分析
            matchToken(Token_Type.SEMICO);      //行尾结束的分号
        }
        Log.back("program");
    }

    public void drawGraphics() {
        JFrame mw = new JFrame("绘图窗口");
        mw.setSize(800, 600);

        XDGraphics.MyPanel mp = new XDGraphics.MyPanel();

        mw.getContentPane().add(mp);
        mw.setVisible(true);
    }


    private void statements() {
        Log.enter("statement");
        switch (token.type) {
            case ORIGIN:
                originStatement();
                break;
            case SCALE:
                scaleStatement();
                break;
            case ROT:
                rotStatement();
                break;
            case FOR:
                forStatement();
                break;
            default:
                error(2);
        }
        Log.back("statement");
    }

    private void originStatement() {
        ExprNode tmp;

        Log.enter("originStatement");

        matchToken(Token_Type.ORIGIN);
        matchToken(Token_Type.IS);
        matchToken(Token_Type.L_BRACKET);
        tmp = expression();
        double origin_x = Calculate.getExprValue(tmp);
        matchToken(Token_Type.COMMA);
        tmp = expression();
        double origin_y = Calculate.getExprValue(tmp);
        matchToken(Token_Type.R_BRACKET);

        XDGraphics.setOrigin(origin_x, origin_y);
    }

    private void scaleStatement() {
        ExprNode tmp;

        Log.enter("scaleStatement");

        matchToken(Token_Type.SCALE);
        matchToken(Token_Type.IS);
        matchToken(Token_Type.L_BRACKET);
        tmp = expression();
        double scale_x = Calculate.getExprValue(tmp);
        matchToken(Token_Type.COMMA);
        tmp = expression();
        double scale_y = Calculate.getExprValue(tmp);
        matchToken(Token_Type.R_BRACKET);

        XDGraphics.setScale(scale_x, scale_y);

        Log.back("scaleStatement");
    }

    private void rotStatement() {
        ExprNode tmp;

        Log.enter("rotStatement");

        matchToken(Token_Type.ROT);
        matchToken(Token_Type.IS);
        tmp = expression();

        double rot_ang = Calculate.getExprValue(tmp);
        XDGraphics.setRot_ang(rot_ang);

        Log.back("rotStatement");
    }

    private void forStatement() {
        ExprNode start_ptr, end_ptr, step_ptr, x_ptr, y_ptr;

        Log.enter("forStatement");

        matchToken(Token_Type.FOR);
        matchToken(Token_Type.T);
        matchToken(Token_Type.FROM);
        start_ptr = expression();
        double start = Calculate.getExprValue(start_ptr);
        matchToken(Token_Type.TO);
        end_ptr = expression();
        double end = Calculate.getExprValue(end_ptr);
        matchToken(Token_Type.STEP);
        step_ptr = expression();
        double step = Calculate.getExprValue(step_ptr);
        matchToken(Token_Type.DRAW);
        matchToken(Token_Type.L_BRACKET);
        x_ptr = expression();
        matchToken(Token_Type.COMMA);
        y_ptr = expression();
        matchToken(Token_Type.R_BRACKET);

        XDGraphics.addFuncPoint(start, end, step, x_ptr, y_ptr);

        Log.back("forStatement");
    }

    private void error(int e) {
        if (e == 1) {
            System.out.println("第" + token.linNo + "行，错误记号：" + token.lexeme);
        } else if (e == 2) {
            System.out.println("第" + token.linNo + "行匹配失败，记号类型：" + token.type.name() + " 值： " + token.lexeme);
        }
    }

    private void matchToken(Token_Type type) {
        if (token.type != type)
            error(2);
        else
            Log.call_match(type.name());
        fetchToken();
    }

    private void fetchToken() {
        if (index < tokenList.size()) {
            token = tokenList.get(index++);
        } else {
            token = tokenList.get(tokenList.size() - 1);
        }

        if (token.type == Token_Type.ERRTOKEN)
            error(1);
    }

    private ExprNode expression() {
        ExprNode left, right;
        Token_Type token_type;

        Log.enter("expression");

        left = term();
        while (token.type == Token_Type.PLUS || token.type == Token_Type.MINUS) {
            token_type = token.type;
            matchToken(token_type);
            right = term();
            left = makeExprNode(token_type, left, right);
        }

        // todo 第二个参数待定

        Log.tree_trace(left, 1);

        Log.back("expression");

        return left;
    }


    private ExprNode term() {
        ExprNode left, right;
        Token_Type token_type;
        left = factor();
        while (token.type == Token_Type.MUL || token.type == Token_Type.DIV) {
            token_type = token.type;
            matchToken(token_type);
            right = factor();
            left = makeExprNode(token_type, left, right);
        }

        return left;
    }

    private ExprNode factor() {  // 递归匹配
        ExprNode left, right;
        if (token.type == Token_Type.PLUS) {
            matchToken(Token_Type.PLUS);
            right = factor();
        } else if (token.type == Token_Type.MINUS) {        // 负数 0-x
            matchToken(Token_Type.MINUS);
            right = factor();
            left = new ExprNode();
            left.opCode = Token_Type.CONST_ID;
            left.caseConst = 0;
            right = makeExprNode(Token_Type.MINUS, left, right);
        } else {
            right = component();
        }

        return right;
    }

    private ExprNode component() {
        ExprNode left, right;
        left = atom();

        if (token.type == Token_Type.POWER) {
            matchToken(Token_Type.POWER);
            right = component();
            left = makeExprNode(Token_Type.POWER, left, right);
        }


        return left;
    }

    private ExprNode atom() {
        Token t = token;
        ExprNode atom = null, tmp;

        switch (token.type) {
            case CONST_ID:
                matchToken(Token_Type.CONST_ID);
                atom = makeExprNode(Token_Type.CONST_ID, t.value);
                break;
            case T:
                matchToken(Token_Type.T);
                atom = makeExprNode(Token_Type.T);
                break;
            case FUNC:
                matchToken(Token_Type.FUNC);
                matchToken(Token_Type.L_BRACKET);
                tmp = expression();
                atom = makeExprNode(Token_Type.FUNC, tmp, t.method);
                matchToken(Token_Type.R_BRACKET);
                break;
            case L_BRACKET:
                matchToken(Token_Type.L_BRACKET);
                atom = expression();
                matchToken(Token_Type.R_BRACKET);
                break;
            default:
                error(2);
        }
        return atom;
    }

    private ExprNode makeExprNode(Token_Type opcode) {
        return this.makeExprNode(opcode, null, null, null, 0, 0, null);
    }

    private ExprNode makeExprNode(Token_Type opcode, ExprNode child, MathMethod method) {
        return this.makeExprNode(opcode, child, null, null, 0, 0, method);
    }

    private ExprNode makeExprNode(Token_Type opcode, double caseConst) {
        return this.makeExprNode(opcode, null, null, null, caseConst, 0, null);
    }

    private ExprNode makeExprNode(Token_Type opcode, ExprNode left, ExprNode right) {
        return this.makeExprNode(opcode, null, left, right, 0, 0, null);
    }

    private ExprNode makeExprNode(Token_Type opcode, ExprNode child, ExprNode left, ExprNode right,
                                  double caseConst, double caseParmPtr, MathMethod method) {
        ExprNode node = new ExprNode();
        node.opCode = opcode;

        switch (opcode) {
            case CONST_ID:      // 常数节点
                node.caseConst = caseConst;
                break;
            case T:             // 参数节点
                node.caseParmPtr = caseParmPtr;
                break;
            case FUNC:          // 函数调用节点
                node.method = method;
                node.child = child;
                break;
            default:            // 二元运算节点
                node.left = left;
                node.right = right;
                break;
        }

        return node;
    }


}
