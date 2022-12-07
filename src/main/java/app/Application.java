package app;


import io.github.humbleui.jwm.*;

import java.io.File;
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
        switch (Platform.CURRENT) {
            case WINDOWS -> window.setIcon(new File("src/main/resources/windows.ico"));
            case MACOS -> window.setIcon(new File("src/main/resources/macos.icns"));
        }
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
