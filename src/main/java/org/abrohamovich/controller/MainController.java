package org.abrohamovich.controller;

import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.abrohamovich.config.EntityManagerFactoryProvider;
import org.abrohamovich.controller.create.CreateBookController;
import org.abrohamovich.controller.create.CreatePatronController;
import org.abrohamovich.controller.manage.*;
import org.abrohamovich.mapper.*;
import org.abrohamovich.repository.*;
import org.abrohamovich.service.*;
import org.abrohamovich.service.interfaces.*;

import java.io.IOException;

@Slf4j
public class MainController {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        MainController.primaryStage = stage;
    }

    public static void loadMainMenuScene() {
        if (primaryStage == null) {
            log.error("Primary stage is null in loadMainMenuScene. Cannot load main menu.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/main.fxml"));
            Parent root = loader.load();

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Library Application");
            primaryStage.show();
        } catch (IOException e) {
            log.error("Failed to load main menu scene: {}", e.getMessage());
        }
    }

    public void goToAddBookScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            AuthorService authorService = new AuthorServiceCRUD(new AuthorRepository(emf), AuthorMapper.INSTANCE);
            GenreService genreService = new GenreServiceCRUD(new GenreRepository(emf), GenreMapper.INSTANCE);
            CategoryService categoryService = new CategoryServiceCRUD(new CategoryRepository(emf), CategoryMapper.INSTANCE);
            PublisherService publisherService = new PublisherServiceCRUD(new PublisherRepository(emf), PublisherMapper.INSTANCE);
            BookService bookService = new BookServiceCRUD(new BookRepository(emf), BookMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/create/create-book.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == CreateBookController.class) {
                    return new CreateBookController(authorService, genreService, categoryService, publisherService, bookService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Add Books");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToAddPatronScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            PatronService patronService = new PatronServiceCRUD(new PatronRepository(emf), PatronMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/create/create-patron.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == CreatePatronController.class) {
                    return new CreatePatronController(patronService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Books");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToManageBooksScene(MouseEvent mouseEvent) {

    }

    public void goToManageAuthorsScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            AuthorRepository authorRepository = new AuthorRepository(emf);
            AuthorService authorService = new AuthorServiceCRUD(authorRepository, AuthorMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage/manage-authors.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == ManageAuthorsController.class) {
                    return new ManageAuthorsController(authorService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Authors");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToManageGenresScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            GenreRepository genreRepository = new GenreRepository(emf);
            GenreService genreService = new GenreServiceCRUD(genreRepository, GenreMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage/manage-genres.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == ManageGenreController.class) {
                    return new ManageGenreController(genreService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Genres");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToManageCategoriesScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            CategoryRepository categoryRepository = new CategoryRepository(emf);
            CategoryService categoryService = new CategoryServiceCRUD(categoryRepository, CategoryMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage/manage-categories.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == ManageCategoryController.class) {
                    return new ManageCategoryController(categoryService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Categories");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToManagePublishersScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            PublisherRepository publisherRepository = new PublisherRepository(emf);
            PublisherService publisherService = new PublisherServiceCRUD(publisherRepository, PublisherMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage/manage-publishers.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == ManagePublisherController.class) {
                    return new ManagePublisherController(publisherService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Publishers");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToManagePatronsScene(MouseEvent mouseEvent) {
        try {
            EntityManagerFactory emf = EntityManagerFactoryProvider.getEntityManagerFactory();
            PatronService patronService = new PatronServiceCRUD(new PatronRepository(emf), PatronMapper.INSTANCE);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/manage/manage-patrons.fxml"));

            loader.setControllerFactory(controlClass -> {
                if (controlClass == ManagePatronController.class) {
                    return new ManagePatronController(patronService);
                }
                try {
                    return controlClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Could not create controller", e);
                }
            });

            Parent root = loader.load();

            if (primaryStage != null) {
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Library Application - Manage Patrons");
                primaryStage.show();
            } else {
                log.error("Primary stage is null");
            }
        } catch (IOException e) {
            log.error("Failed to load scene: {}", e.getMessage());
        }
    }

    public void goToViewBooksScene(MouseEvent mouseEvent) {
    }

    public void goToViewPatronsScene(MouseEvent mouseEvent) {
    }

    public void goToLendReturnScene(MouseEvent mouseEvent) {
    }

}
