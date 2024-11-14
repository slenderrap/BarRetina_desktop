module com.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires Java.WebSocket;
    requires java.xml;

    //requires org.json;
    opens com.project.Utils to javafx.fxml;
    opens com.project.Views to javafx.fxml;
    exports com.project;
    exports com.project.Utils;
    exports com.project.Views;
}