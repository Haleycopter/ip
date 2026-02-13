package howly;

import javafx.animation.PauseTransition; //for exit delay
import javafx.application.Platform; //for graceful shutdown
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration; //for time measurement

/**
 * Controller for the main GUI of the Howly application.
 * Provides the layout for the other controls and handles user input.
 */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    @FXML private Button sendButton;

    private Howly howly;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/calcifer.png"));
    private Image howlyImage = new Image(this.getClass().getResourceAsStream("/images/howl.png"));
    private Image poorHowly = new Image(this.getClass().getResourceAsStream("/images/poorhowl.png"));

    /**
     * Initializes the controller. Binds the scroll pane to the dialog container's height
     * and displays the initial greeting message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        String greeting = "Hello! I'm Howly\nWhat can I do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getHowlyDialog(greeting, howlyImage, false)
        );
    }

    /**
     * Sets the Howly instance to be used by the controller.
     *
     * @param h The Howly chatbot instance.
     */
    public void setHowly(Howly h) {
        howly = h;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Howly's
     * reply, and then appends them to the dialog container. Clears the user input
     * after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = howly.getResponse(input);

        //AI assisted: implement error message check to display error image
        boolean isError = response.contains("Gahhh! What's the point in living if I can't be beautiful...");

        Image currentHowlyImage = isError ? poorHowly : howlyImage;
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(input, userImage),
                DialogBox.getHowlyDialog(response, currentHowlyImage, isError)
        );
        userInput.clear();

        //close GUI app if user issues "bye" command
        if (input.trim().equalsIgnoreCase("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }

    /**
     * Adds multiple dialog boxes to the container at once.
     * @param containers The DialogBox nodes to be added.
     */
    public void addDialogs(DialogBox... containers) {
        dialogContainer.getChildren().addAll(containers);
    }
}
