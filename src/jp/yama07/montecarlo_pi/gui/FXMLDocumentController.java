package jp.yama07.montecarlo_pi.gui;

import jp.yama07.montecarlo_pi.SimulationTimeline;
import jp.yama07.montecarlo_pi.util.LongSpinnerValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.util.converter.NumberStringConverter;

/**
 * Monte Carlo法による円周率算出シミュレータ画面のコントローラクラス
 *
 * @author yama07
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Canvas cPlot;
    @FXML
    private LineChart<Number, Number> lcResult;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Spinner<Long> sTrials;
    @FXML
    private Label lNumOfTrials;
    @FXML
    private Label lNumOfInside;
    @FXML
    private Label lNumOfOutside;
    @FXML
    private Label lEstimatedPI;
    @FXML
    private ProgressBar pbCalcProgress;

    private SimulationTimeline timeline;

    /**
     * シミュレートstart／pauseボタンのハンドル<BR>
     * running中はシミュレートを一時停止し、それ以外のときはシミュレートを開始する。
     *
     * @param event
     */
    @FXML
    private void handleStartButtonAction(ActionEvent event) {
        if (timeline.getState().equals(Status.RUNNING)) {
            timeline.pause();
        } else if (timeline.getState().equals(Status.STOPPED)) {
            timeline.setTrials(sTrials.getValue());
            timeline.play();
        } else if (timeline.getState().equals(Status.PAUSED)) {
            timeline.play();
        }
    }

    /**
     * シミュレートresetボタンのハンドル<BR>
     * シミュレートをリセットする。
     *
     * @param event
     */
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        timeline.stop();
        timeline.clear();
    }

    /**
     * 初期化メソッド<BR>
     * 試行回数スピナーとシミュレータの描画タイムラインの設定、各コンポーネンのプロパティとのバインディングを行う。
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline = new SimulationTimeline(cPlot, lcResult);
        sTrials.setValueFactory(new LongSpinnerValueFactory(
                SimulationTimeline.TRIALS_MIN,
                SimulationTimeline.TRIALS_MAX,
                SimulationTimeline.DEFAULT_TRIALS,
                SimulationTimeline.TRIALS_STEP));
        lNumOfInside.textProperty().bindBidirectional(timeline.numOfInsideProperty(), new NumberStringConverter());
        lNumOfOutside.textProperty().bindBidirectional(timeline.numOfOutsideProperty(), new NumberStringConverter());
        lNumOfTrials.textProperty().bindBidirectional(timeline.numOfTotalProperty(), new NumberStringConverter());
        lEstimatedPI.textProperty().bind(new SimpleStringProperty("").concat(timeline.estimatedPIProperty().asString("%2.16f")));
        pbCalcProgress.progressProperty().bindBidirectional(timeline.progressProperty());
    }

}
