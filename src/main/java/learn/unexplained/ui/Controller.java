package learn.unexplained.ui;

import learn.unexplained.data.DataAccessException;
import learn.unexplained.domain.EncounterResult;
import learn.unexplained.domain.EncounterService;
import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;

import java.util.List;

public class Controller {

    private final EncounterService service;
    private final View view;

    public Controller(EncounterService service, View view) {
        this.service = service;
        this.view = view;
    }

    public void run() {
        view.printHeader("Welcome To Unexplained Encounters.");

        try {
            runMenuLoop();
        } catch (DataAccessException ex) {
            view.printHeader("CRITICAL ERROR:" + ex.getMessage());
        }

        view.printHeader("Goodbye");
    }

    private void runMenuLoop() throws DataAccessException {
        MenuOption option;
        do {
            option = view.displayMenuGetOption();
            switch (option) {
                case DISPLAY_ALL:
                    displayAllEncounters();
                    break;
                case DISPLAY_BY_TYPE:
                    displayEncountersByType();
                    break;
                case ADD:
                    addEncounter();
                    break;
                case UPDATE:
                    updateEncounter();
                    break;
                case DELETE:
                    deleteEncounter();
                    break;
            }
        } while (option != MenuOption.EXIT);
    }

    private void displayAllEncounters() throws DataAccessException {
        List<Encounter> encounters = service.findAll();
        view.printAllEncounters(encounters);
    }

    private void displayEncountersByType() throws DataAccessException {
        view.printHeader(MenuOption.DISPLAY_BY_TYPE.getMessage());
        EncounterType encounterType = view.readType();
        List<Encounter> encounters = service.findByType(encounterType);
        view.printEncountersByType(encounters, encounterType);
    }

    private void addEncounter() throws DataAccessException {
        Encounter encounter = view.makeEncounter();
        EncounterResult result = service.add(encounter);
        view.printResult(result, "Encounter Id %s added.%n");
    }

    private void updateEncounter() throws DataAccessException {
        view.printHeader(MenuOption.UPDATE.getMessage());
        List<Encounter> encounters = service.findAll();
        Encounter encounter = view.selectEncounter(encounters);
        if (encounter != null) {
            Encounter updatedEncounter = view.updateEncounter(encounter);
            EncounterResult result = service.update(updatedEncounter);
            view.printResult(result, "Encounter Id %s updated.%n");
        }
    }

    private void deleteEncounter() throws DataAccessException {
        view.printHeader(MenuOption.DELETE.getMessage());
        List<Encounter> encounters = service.findAll();
        Encounter encounter = view.selectEncounter(encounters);
        if (encounter != null) {
            EncounterResult result = service.deleteById(encounter.getEncounterId());

            // HACK this doesn't feel great... but it solves my immediate problem
            result.setPayload(encounter);

            view.printResult(result, "Encounter Id %s deleted.%n");
        }
    }
}