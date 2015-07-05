package jp.yama07.montecarlo_pi;

import java.util.stream.Stream;

/**
 * ランダムな座標を作り出すクラス
 *
 * @author yama07
 */
public class RandomPointGenerator {

    private final int range;

    /**
     *
     * @param range 作り出すランダム座標の範囲
     */
    public RandomPointGenerator(int range) {
        this.range = range;
    }

    public int getRadius() {
        return range;
    }

    /**
     * 0 &le; x &le; range , 0 &le; y &le; rangeのランダム座標を返す
     *
     * @return ランダム座標のStream
     */
    public Stream<Point> getPointStream() {
        return Stream.generate(() -> new Point(Math.random() * range, Math.random() * range));
    }

}
