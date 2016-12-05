package graphic;

import parser.ExprNode;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static util.Calculate.getExprValue;

/**
 * Created by KongFanyang on 2016/12/5.
 */
public class XDGraphics {

    public double parameter;

    private static double scale_x = 1, scale_y = 1, rot_ang = 0, origin_x = 0, origin_y = 0; // 如果第二次for-draw语句没有设置以上参数，则沿用第一次的参数
    private static Map<FuncPoint, Configure> pointMap = new HashMap<>();
    private static Configure tempConfig;

    public static void setScale(double x, double y) {
        scale_x = x;
        scale_y = y;
    }

    public static void setRot_ang(double angle) {
        rot_ang = angle;
    }

    public static void setOrigin(double x, double y) {
        origin_x = x;
        origin_y = y;
    }


    public static double[] calcPoint(ExprNode x_nptr, ExprNode y_nptr, double i) {
        double temp, local_x, local_y;
        local_x = getExprValue(x_nptr, i);        // 计算点的原始坐标
        local_y = getExprValue(y_nptr, i);
        local_x *= tempConfig.scale_x;                     //比例变换
        local_y *= tempConfig.scale_y;
        temp = local_x * Math.cos(tempConfig.rot_ang) + local_y * Math.sin(tempConfig.rot_ang);
        local_y = local_y * Math.cos(tempConfig.rot_ang) - local_x * Math.sin(tempConfig.rot_ang);
        local_x = temp;                         // 旋转变换
        local_x += tempConfig.origin_x;                    // 平移变换
        local_y += tempConfig.origin_y;
        return new double[]{local_x, local_y};
    }


    public static void addFuncPoint(double start, double end, double step, ExprNode x_ptr, ExprNode y_ptr) {
        pointMap.put(new FuncPoint(start, end, step, x_ptr, y_ptr), new Configure(scale_x, scale_y, rot_ang, origin_x, origin_y));
    }

    public static class MyPanel extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.red);
            for (Map.Entry<FuncPoint, Configure> entry : pointMap.entrySet()) {
                FuncPoint p = entry.getKey();
                tempConfig = entry.getValue();
                for (double i = p.start; i <= p.end; i += p.step) {
                    double[] xy = calcPoint(p.x_ptr, p.y_ptr, i);
                    g2d.drawLine((int) xy[0], (int) xy[1], (int) xy[0], (int) xy[1]);
                }
            }

        }
    }

    /**
     * 保存 起点，终点，步长以及左右参数的节点数
     */
    private static class FuncPoint {
        double start, end, step;
        ExprNode x_ptr, y_ptr;

        FuncPoint(double start, double end, double step, ExprNode x_ptr, ExprNode y_ptr) {
            this.start = start;
            this.end = end;
            this.step = step;
            this.x_ptr = x_ptr;
            this.y_ptr = y_ptr;
        }
    }


    /**
     * 保存对应FuncPoint的原点，旋转角度，平移距离，缩放比例
     */
    private static class Configure {
        public Configure(double scale_x, double scale_y, double rot_ang, double origin_x, double origin_y) {
            this.scale_x = scale_x;
            this.scale_y = scale_y;
            this.rot_ang = rot_ang;
            this.origin_x = origin_x;
            this.origin_y = origin_y;
        }

        double scale_x = 1, scale_y = 1, rot_ang = 0, origin_x = 0, origin_y = 0;
    }
}
