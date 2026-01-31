package howly;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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

    /**
     * Initializes the controller. Binds the scroll pane to the dialog container's height
     * and displays the initial greeting message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        String greeting = "Hello! I'm Howly\nWhat can I do for you?";
        dialogContainer.getChildren().addAll(
                DialogBox.getHowlyDialog(greeting, howlyImage)
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
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getHowlyDialog(response, howlyImage)
        );
        userInput.clear();
    }
}
