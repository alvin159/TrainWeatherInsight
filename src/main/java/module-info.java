module compse.frontend {
    requires javafx.controls;
    exports compse110.frontend;
    exports compse110.frontend.Entity;
    requires com.google.gson;
    requires okhttp3;
    //allow Gson access model
    opens compse110.frontend.Entity;
    opens compse110.Entity;
}
