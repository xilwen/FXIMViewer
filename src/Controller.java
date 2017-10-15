import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    private int lastContrast;
    private String path;
    private GraphicsContext gc;
    private Image image;
    private CanvaProcessor canvaProcessor = new CanvaProcessor();
    private File file;
    private boolean sliderActionLock = false;
    private Canvas shadowCanva = new Canvas();

    void init() {
        gc = canvas.getGraphicsContext2D();
        openButton.setOnMouseClicked(e -> {
            FileChooser filechooser = new FileChooser();
            file = filechooser.showOpenDialog(null);
            if (file == null) {
                showAlert("請選擇檔案路徑");
                return;
            }
            path = createImagePath(file);
            drawImageByFile();
            contrastSlider.setValue(50);
            contrastTextField.setText("0");
        });

        contrastSlider.valueProperty().addListener(e -> {
            if (!sliderActionLock) {
                int castedSliderValue = (int) ((contrastSlider.getValue() - 50.0) / 50 * 255);
                if (lastContrast == castedSliderValue) {
                    return;
                }
                contrastTextField.setText(Integer.toString(castedSliderValue));
                refreshCanvasWithNewContrast(castedSliderValue);
            }
        });

        contrastSlider.setOnMouseReleased(e->System.gc());

        contrastTextField.textProperty().addListener(e -> {
            if (contrastTextField.getLength() == 0) {
                return;
            }
            if (contrastTextField.getLength() > 4) {
                showAlert("請輸入-255~255之間的數字");
                return;
            }
            char[] testArray = new char[5];
            contrastTextField.getText().getChars(0, contrastTextField.getLength(), testArray, 0);
            for (int i = contrastTextField.getLength() - 1; i >= 0; --i) {
                if (!Character.isDigit(testArray[i])) {
                    if ((int) testArray[i] == 0) {
                        break;
                    }
                    if ((testArray[i] == '-' || testArray[i] == '+') && i == 0) {
                        if (contrastTextField.getLength() == 1) {
                            return;
                        }
                        break;
                    }
                    showAlert("請輸入-255~255之間的數字");
                    return;
                }
            }
            int contrast = Integer.valueOf(contrastTextField.getText());
            if (contrast > 255 || contrast < (-255)) {
                showAlert("請輸入-255~255之間的數字");
                return;
            }
            sliderActionLock = true;
            int sliderValue = (int) ((double) contrast / 255.0 * 50.0) + 50;
            contrastSlider.setValue(sliderValue);
            refreshCanvasWithNewContrast(contrast);
            sliderActionLock = false;
        });

        showSkinToggleButton.setOnMouseClicked(e -> {
            if (file == null || image.isError()) {
                return;
            }
            if (showSkinToggleButton.isSelected()) {
                drawImage(canvaProcessor.showSkinOnly(canvas));
            } else {
                refreshCanvasWithNewContrast(lastContrast);
            }
        });


        resetContrast();
    }

    private void refreshCanvasWithNewContrast(int contrast) {
        if (file == null || image.isError()) {
            return;
        }
        lastContrast = contrast;
        boolean skinToggled = showSkinToggleButton.isSelected();
        WritableImage newImage = canvaProcessor.setContrast(shadowCanva, contrast);
        if(skinToggled) {
            canvas.setVisible(false);
        }
        drawImage(newImage);
        if (skinToggled) {
            canvas.setVisible(true);
            drawImage(canvaProcessor.showSkinOnly(canvas));
            showSkinToggleButton.setSelected(true);
        }
        System.gc();
    }

    private void drawImageByFile() {
        showSkinToggleButton.setSelected(false);
        image = new Image(path);
        if (image.isError()) {
            showAlert("圖片格式不支援或未知錯誤");
        }
        canvas.setHeight(image.getHeight());
        canvas.setWidth(image.getWidth());
        drawImage(image);
        shadowCanva.setHeight(image.getHeight());
        shadowCanva.setWidth(image.getWidth());
        GraphicsContext sgc = shadowCanva.getGraphicsContext2D();
        sgc.drawImage(image, 0, 0);
    }

    private void showAlert(String string) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("汝，迷茫的家裡蹲啊。不要太過自責……");
        alert.setHeaderText(string);
        alert.setContentText("臣亮言先帝創業未半而中道崩殂今天下三分益州疲弊此誠危急存亡之秋也然侍衛之臣不懈於內忠志之士忘身於外者蓋追先帝之殊遇欲報之於陛下也誠宜開張聖聽以光先帝遺德恢弘志士之氣");
        alert.show();
    }

    private void resetContrast() {
        contrastSlider.setValue(50);
        contrastTextField.setText("0");
        refreshCanvasWithNewContrast(0);
    }

    private String createImagePath(File file) {
        String os = System.getProperty("os.name"),
                newPath;
        if (os.indexOf("win") >= 0) {
            newPath = "file:/" + file.getPath().replace("\\", "/");
        } else {
            newPath = "file:" + file.getPath();
        }
        return newPath;
    }

    void drawImage(Image image) {
        gc.drawImage(image, 0, 0);
    }

    @FXML
    private Canvas canvas;

    @FXML
    private Button openButton;

    @FXML
    private Slider contrastSlider;

    @FXML
    private TextField contrastTextField;

    @FXML
    private ToggleButton showSkinToggleButton;

    @FXML
    private BorderPane borderpane;

}