package jp.yama07.montecarlo_pi;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.util.Duration;

/**
 * Monte Carlo法による円周率算出のシミュレートを行うクラス
 *
 * @author yama07
 */
public class SimulationTimeline {

    // 試行回数に関するパラメータ
    public static final long TRIALS_MIN = 100000;
    public static final long TRIALS_MAX = 20000000L;
    public static final long TRIALS_STEP = 10000;
    public static final long DEFAULT_TRIALS = TRIALS_MIN;

    //グラフに関するパラメータ
    public static final Number TICK_UNIT_Y = 0.01;
    private double yLowerBound = 3.13;
    private double yUpperBound = 3.15;

    //Canvasに描画する際の色
    private Color circleColor = Color.LIGHTBLUE;
    private Color backgroundColor = Color.WHITE;
    private Color dotColor = new Color(Color.CORNFLOWERBLUE.getRed(), Color.CORNFLOWERBLUE.getGreen(), Color.CORNFLOWERBLUE.getBlue(), 0.5);

    // ランダム座標に点をプロットするCanvas
    private final Canvas cPlot;
    // 推定した円周率をプロットするグラフ
    private final LineChart<Number, Number> lcResult;

    // 円の内部にプロットされた数
    private final LongProperty numOfInside = new SimpleLongProperty(0L);
    // 円の外部にプロットされた数
    private final LongProperty numOfOutside = new SimpleLongProperty(0L);
    // プロット数
    private final LongProperty numOfTotal = new SimpleLongProperty(0L);
    // シミュレートにより推定した円周率
    private final DoubleProperty estimatedPI = new SimpleDoubleProperty(0.0f);
    // 進捗率
    private final DoubleProperty progress = new SimpleDoubleProperty(0.0f);

    // グラフに描画する真の円周率のデータ
    private XYChart.Series series_PI;
    // グラフに描画する推定円周率のデータ
    private XYChart.Series series_estimatedPI;

    // シミュレーションを行うTimeline
    private Timeline timer;

    // 試行回数
    private long trials;

    /**
     * シミュレート結果および経過を描画するコンポーネントを指定。 試行回数はSimulationTimeline.DEFAULT_TRIALS回。
     *
     * @param cPlot ランダム座標に点をプロットするCanvas
     * @param lcResult 推定した円周率をプロットするLineChart
     */
    public SimulationTimeline(Canvas cPlot, LineChart<Number, Number> lcResult) {
        this(cPlot, lcResult, DEFAULT_TRIALS);
    }

    /**
     * シミュレート結果および経過を描画するコンポーネントを指定。 試行回数はtrials回。
     *
     * @param cPlot ランダム座標に点をプロットするCanvas
     * @param lcResult 推定した円周率をプロットするLineChart
     * @param trials 試行回数
     */
    public SimulationTimeline(Canvas cPlot, LineChart<Number, Number> lcResult, long trials) {
        this.cPlot = cPlot;
        this.lcResult = lcResult;
        this.trials = trials;
        clear();
    }

    /**
     * シミュレート結果をクリアする
     */
    public void clear() {
        Platform.runLater(() -> {
            initChart();
            initPlot();
        });
        numOfInside.set(0L);
        numOfOutside.set(0L);
        numOfTotal.set(0L);
        estimatedPI.set(0.0f);
        progress.set(0.0f);
    }

    /**
     * Canvasを初期化する
     */
    private void initPlot() {
        GraphicsContext gc = cPlot.getGraphicsContext2D();
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, cPlot.getWidth(), cPlot.getHeight());
        gc.setFill(circleColor);
        gc.fillArc(-1.0 * cPlot.getWidth(), 0, cPlot.getWidth() * 2, cPlot.getHeight() * 2, 0, 90, ArcType.ROUND);
    }

    /**
     * LineChartを初期化する
     */
    private void initChart() {
        lcResult.getData().clear();
        lcResult.setCreateSymbols(false);
        lcResult.setLegendSide(Side.TOP);

        NumberAxis xAxis = (NumberAxis) lcResult.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(trials);
        xAxis.setTickUnit(trials);

        NumberAxis yAxis = (NumberAxis) lcResult.getYAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(yLowerBound);
        yAxis.setUpperBound(yUpperBound);
        yAxis.setTickUnit(TICK_UNIT_Y.doubleValue());

        series_PI = new XYChart.Series();
        series_PI.setName(String.valueOf(Math.PI));
        series_PI.getData().add(new XYChart.Data(0, Math.PI));
        series_PI.getData().add(new XYChart.Data(trials, Math.PI));
        lcResult.getData().add(series_PI);

        series_estimatedPI = new XYChart.Series();
        series_estimatedPI.setName("Estimated PI");
        lcResult.getData().add(series_estimatedPI);
    }

    public void setTrials(long trials) {
        this.trials = trials;
    }

    public long getTrials() {
        return trials;
    }

    public Color getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(Color circleColor) {
        this.circleColor = circleColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getDotColor() {
        return dotColor;
    }

    public void setDotColor(Color dotColor) {
        this.dotColor = dotColor;
    }

    public double getyLowerBound() {
        return yLowerBound;
    }

    public void setyLowerBound(double yLowerBound) {
        this.yLowerBound = yLowerBound;
    }

    public double getyUpperBound() {
        return yUpperBound;
    }

    public void setyUpperBound(double yUpperBound) {
        this.yUpperBound = yUpperBound;
    }

    public long getNumOfInside() {
        return numOfInside.get();
    }

    public void setNumOfInside(long value) {
        numOfInside.set(value);
    }

    public LongProperty numOfInsideProperty() {
        return numOfInside;
    }

    public long getNumOfOutside() {
        return numOfOutside.get();
    }

    public void setNumOfOutside(long value) {
        numOfOutside.set(value);
    }

    public LongProperty numOfOutsideProperty() {
        return numOfOutside;
    }

    public long getNumOfTotal() {
        return numOfTotal.get();
    }

    public void setNumOfTotal(long value) {
        numOfTotal.set(value);
    }

    public LongProperty numOfTotalProperty() {
        return numOfTotal;
    }

    public double getEstimatedPI() {
        return estimatedPI.get();
    }

    public void setEstimatedPI(double value) {
        estimatedPI.set(value);
    }

    public DoubleProperty estimatedPIProperty() {
        return estimatedPI;
    }

    public double getProgress() {
        return progress.get();
    }

    public void setProgress(double value) {
        progress.set(value);
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    /**
     * シミュレートの状態を返す
     *
     * @return 状態
     */
    public Status getState() {
        if (timer == null) {
            return Status.STOPPED;
        }
        return timer.getStatus();
    }

    /**
     * シミュレートの開始および再開を行う
     */
    public void play() {
        if (timer == null || timer.getStatus().equals(Status.STOPPED)) {
            clear();
            setTimelineParam();
        }
        timer.play();
    }

    /**
     * シミュレートを中止する
     */
    public void stop() {
        if (timer == null) {
            return;
        }
        timer.stop();
    }

    /**
     * シミュレートを一時中断する
     */
    public void pause() {
        if (timer == null) {
            return;
        }
        timer.pause();
    }

    /**
     * シュミレートTimelineのパラメータをセットする
     */
    private void setTimelineParam() {
        timer = new Timeline(
                new KeyFrame(
                        Duration.millis(1), (ActionEvent e) -> {
                            GraphicsContext gc = cPlot.getGraphicsContext2D();
                            gc.setFill(dotColor);

                            final int radius = (int) cPlot.getWidth();
                            final long trial = (long) (trials / TRIALS_STEP);
                            long _numOfInside = numOfInside.getValue()
                            + new RandomPointGenerator(radius).getPointStream().limit(trial)
                            .peek(p -> gc.fillRect(p.getX(), p.getY(), 1, 1))
                            .filter(p -> p.distFromOrigin() <= radius).count();
                            long _numOfTotal = numOfTotal.getValue() + trial;

                            numOfInside.set(_numOfInside);
                            numOfOutside.set(_numOfTotal - _numOfInside);
                            numOfTotal.set(_numOfTotal);

                            double pi = 4.0 * _numOfInside / _numOfTotal;
                            series_estimatedPI.getData().add(new XYChart.Data(_numOfTotal, pi));
                            estimatedPI.set(pi);
                            progress.set(1.0 * _numOfTotal / trials);
                        })
        );
        timer.setCycleCount((int) TRIALS_STEP);
    }

}
