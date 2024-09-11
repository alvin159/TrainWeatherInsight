module compse110.frontend {
    requires javafx.controls;
    exports compse110.frontend;
    requires com.google.gson;
    requires okhttp3;
    //allow Gson access model
    opens compse110.frontend.Entity;
}
