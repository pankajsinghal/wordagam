package io.github.gravetii;

import io.github.gravetii.game.GameService;
import io.github.gravetii.scene.FxScene;
import io.github.gravetii.scene.start.StartScene;
import io.github.gravetii.store.StoreUtility;
import io.github.gravetii.util.AppLogger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class App extends Application {

  private Stage stage;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    AppLogger.info(getClass().getCanonicalName(), "Starting application...");
    this.stage = stage;
    this.stage.setOnCloseRequest(
        event -> {
          Platform.exit();
        });

    FxScene scene = new StartScene(stage);
    scene.show();
  }

  @Override
  public void init() {
    GameService.init();
  }

  @Override
  public void stop() throws Exception {
    GameService.close();
    this.stage.close();
    StoreUtility.close();
  }
}
