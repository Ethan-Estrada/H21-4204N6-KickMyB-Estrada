package org.estrada.tp1.transfer;

import java.util.Date;

public class AddTaskRequest {
    // Le nom, non vide doit être unique
    public String name;
    // La date limite pour la tâche
    public Date deadline;
}
