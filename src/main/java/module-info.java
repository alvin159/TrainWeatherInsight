module compse.frontend {
    requires javafx.controls;
    exports compse110.frontend;
    exports compse110.frontend.Entity;
    exports compse110.backend.Entity;
    requires com.google.gson;
    requires okhttp3;
    requires javafx.graphics;
    //allow Gson access model
    opens compse110.frontend.Entity;
    opens compse110.Entity;
    opens compse110.backend.Entity;
}
