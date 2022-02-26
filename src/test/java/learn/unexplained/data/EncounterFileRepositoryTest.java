package learn.unexplained.data;

import learn.unexplained.models.Encounter;
import learn.unexplained.models.EncounterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EncounterFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/encounters-seed.csv";
    static final String TEST_FILE_PATH = "./data/encounters-test.csv";

    EncounterFileRepository repository = new EncounterFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setupTest() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll() throws DataAccessException {
        List<Encounter> encounters = repository.findAll();
        assertEquals(3, encounters.size());
    }

    @Test
    void shouldFindByType() throws DataAccessException {
        List<Encounter> encounters = repository.findByType(EncounterType.UFO);
        assertEquals(1, encounters.size());
    }

    @Test
    void shouldNotFindByTypeEmptyList() throws DataAccessException {
        List<Encounter> encounters = repository.findByType(EncounterType.VISION);
        assertEquals(0, encounters.size());
    }

    @Test
    void shouldFindById() throws DataAccessException {
        Encounter encounter = repository.findById(1);
        assertNotNull(encounter);
        assertEquals(1, encounter.getEncounterId());
        assertEquals(EncounterType.UFO, encounter.getType());
        assertEquals("2020-01-01", encounter.getWhen());
        assertEquals("short test #1", encounter.getDescription());
        assertEquals(1, encounter.getOccurrences());
    }

    @Test
    void shouldNotFindByIdNull() throws DataAccessException {
        Encounter encounter = repository.findById(999999);
        assertNull(encounter);
    }

    @Test
    void shouldAdd() throws DataAccessException {
        Encounter encounter = new Encounter();
        encounter.setType(EncounterType.UFO);
        encounter.setWhen("Jan 15, 2005");
        encounter.setDescription("moving pinpoint of light." +
                "seemed to move with me along the highway. " +
                "then suddenly reversed direction without slowing down. it just reversed.");
        encounter.setOccurrences(1);

        Encounter actual = repository.add(encounter);

        assertNotNull(actual);
        assertEquals(4, actual.getEncounterId());
    }

    @Test
    void shouldUpdate() throws DataAccessException {
        Encounter encounterToUpdate = new Encounter(1, EncounterType.UFO, "2020-01-01",
                "short test #1 updated", 1);
        assertTrue(repository.update(encounterToUpdate));

        Encounter actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(encounterToUpdate, actual);
    }

    @Test
    void shouldNotUpdate() throws DataAccessException {
        Encounter encounterToUpdate = new Encounter(999999, EncounterType.UFO, "2020-01-01",
                "short test #1 updated", 1);
        assertFalse(repository.update(encounterToUpdate));
    }

    @Test
    void shouldDeleteById() throws DataAccessException {
        boolean actual = repository.deleteById(1);
        assertTrue(actual);

        assertNull(repository.findById(1));
    }

    @Test
    void shouldNotDeleteById() throws DataAccessException {
        boolean actual = repository.deleteById(999999);
        assertFalse(actual);
    }
}