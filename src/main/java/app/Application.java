package app;


import controls.Label;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.skija.EventFrameSkija;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Surface;
import misc.CoordinateSystem2i;
import panels.PanelControl;
import panels.PanelHelp;
import panels.PanelLog;
import panels.PanelRendering;

import java.io.File;
import java.util.function.Consumer;

import static app.Colors.APP_BACKGROUND_COLOR;
import static app.Colors.PANEL_BACKGROUND_COLOR;

public class Application implements Consumer<Event> {
    /**
     * радиус скругления элементов
     */
    public static final int C_RAD_IN_PX = 4;
    /**
     * отступы панелей
     */
    public static final int PANEL_PADDING = 5;
    /**
     * окно приложения
     */
    private final Window window;
    /**
     * Первый заголовок
     */
    private final Label label;
    /**
     * Первый заголовок
     */
    private final Label label2;
    /**
     * Первый заголовок
     */
    private final Label label3;
    /**
     * панель легенды
     */
    private final PanelHelp panelHelp;
    /**
     * панель курсора мыши
     */
    private final PanelControl panelControl;
    /**
     * панель рисования
     */
    private final PanelRendering panelRendering;
    /**
     * панель событий
     */
    private final PanelLog panelLog;
    public Application() {
        window = App.makeWindow();
        // создаём первый заголовок
        label = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 1, 1, 1, 1, "Привет, мир!", true, true);
        // создаём второй заголовок
        label2 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 0, 3, 1, 1, "Второй заголовок", true, true);

        // создаём третий заголовок
        label3 = new Label(window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING,
                4, 4, 2, 0, 1, 1, "Это тоже заголовок", true, true);
        // создаём панель рисования
        panelRendering = new PanelRendering(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 0, 0,
                3, 2
        );
        // создаём панель управления
        panelControl = new PanelControl(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 3, 0,
                2, 2
        );
        // создаём панель лога
        panelLog = new PanelLog(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 0, 2,
                3, 1
        );
        // создаём панель помощи
        panelHelp = new PanelHelp(
                window, true, PANEL_BACKGROUND_COLOR, PANEL_PADDING, 5, 3, 3, 2,
                2, 1
        );

        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(900, 900);
        window.setWindowPosition(100, 100);


        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }

        String[] layerNames = new String[]{
                "LayerGLSkija", "LayerRasterSkija"
        };

        // перебираем слои
        for (String layerName : layerNames) {
            String className = "io.github.humbleui.jwm.skija." + layerName;
            try {
                Layer layer = (Layer) Class.forName(className).getDeclaredConstructor().newInstance();
                window.setLayer(layer);
                break;
            } catch (Exception e) {
                System.out.println("Ошибка создания слоя " + className);
            }
        }

        // если окну не присвоен ни один из слоёв
        if (window._layer == null)
            throw new RuntimeException("Нет доступных слоёв для создания");

        window.setVisible(true);

    }


    /**
     * Обработчик событий
     *
     * @param event событие
     */
    @Override
    public void accept(Event event) {
        if (event instanceof EventWindowClose) {
            App.terminate();
        } else if (event instanceof EventWindowCloseRequest) {
            window.close();
        } else if (event instanceof EventFrameSkija ee) {
            // получаем поверхность рисования
            Surface s = ee.getSurface();
            // очищаем её канвас заданным цветом
            paint(s.getCanvas(), new CoordinateSystem2i(s.getWidth(), s.getHeight()));
        }
    }

    /**
     * Рисование
     *
     * @param canvas   низкоуровневый инструмент рисования примитивов от Skija
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // запоминаем изменения (пока что там просто заливка цветом)
        canvas.save();
        // очищаем канвас
        canvas.clear(APP_BACKGROUND_COLOR);
        // рисуем панели
        panelRendering.paint(canvas, windowCS);
        panelControl.paint(canvas, windowCS);
        panelLog.paint(canvas, windowCS);
        panelHelp.paint(canvas, windowCS);
        canvas.restore();
    }
}
