package jp.yama07.montecarlo_pi;

/**
 * x,y座標を表すクラス
 *
 * @author yama07
 */
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * 原点からのユークリッド距離を返す
     *
     * @return 原点からの距離
     */
    public double distFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }

}
