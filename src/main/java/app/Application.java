package app;


import io.github.humbleui.jwm.*;

import java.util.function.Consumer;

public class Application implements Consumer<Event> {
    /**
     * Main application window
     */
    private final Window window;

    public Application() {
        window = App.makeWindow();
        window.setEventListener(this);
        window.setVisible(true);
    }


    /**
     * Event handler
     * @param event event instance
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
