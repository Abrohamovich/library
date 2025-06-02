package org.abrohamovich;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.abrohamovich.config.EntityManagerFactoryProvider;
import org.abrohamovich.controller.MainController;
import org.abrohamovich.mapper.BookMapper;
import org.abrohamovich.repository.BookRepository;
import org.abrohamovich.service.BookServiceCRUD;
import org.abrohamovich.service.interfaces.BookService;

public class LibraryApplication extends Application {

    public static void main(String[] args) {
        EntityManagerFactoryProvider.initialize("library");

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();

        primaryStage.setOnCloseRequest(event -> EntityManagerFactoryProvider.closeEntityManagerFactory());
        primaryStage.setTitle("Library Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
