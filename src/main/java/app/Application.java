package app;


import io.github.humbleui.jwm.*;

import java.util.function.Consumer;

public class Application implements Consumer<Event> {
    /**
     * окно приложения
     */
    private final Window window;

    public Application() {
        window = App.makeWindow();
        window.setEventListener(this);
        window.setTitle("Java 2D");
        window.setWindowSize(900, 900);
        window.setWindowPosition(100, 100);
        window.setVisible(true);

    }


    /**
     * Обработчик событий
     * @param event событие
     */
    @Override
    public void accept(Event event) {
        if (event instanceof EventWindowClose) {
            App.terminate();
        } else if (event instanceof EventWindowCloseRequest) {
            window.close();
        }
    }
}
