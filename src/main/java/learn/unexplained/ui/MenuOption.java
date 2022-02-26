package learn.unexplained.ui;

public enum MenuOption {
    EXIT("Exit"),
    DISPLAY_ALL("Display All Encounters"),
    DISPLAY_BY_TYPE("Display Encounters By Type"),
    ADD("Add An Encounter"),
    UPDATE("Update an Encounter"),
    DELETE("Delete an Encounter");

    private String message;

    MenuOption(String name) {
        this.message = name;
    }

    public String getMessage() {
        return message;
    }
}