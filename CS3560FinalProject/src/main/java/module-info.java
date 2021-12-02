module com.example.cs3560finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql.rowset;


    opens com.example.cs3560finalproject to javafx.fxml;
    exports com.example.cs3560finalproject;
}