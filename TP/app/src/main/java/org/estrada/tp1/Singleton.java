package org.estrada.tp1;

public class Singleton {
    String editValue;
    private static final Singleton ourInstance = new Singleton();
    public static Singleton getInstance() {
        return ourInstance;
    }
    private Singleton() { }
    public void setText(String editValue) {
        this.editValue = editValue;
    }
    public String getText() {
        return editValue;
    }
}