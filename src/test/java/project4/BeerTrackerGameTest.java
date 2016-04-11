package project4;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BeerTrackerGameTest {

    @Test
    public void testGetCellsOccupiedByObject() throws Exception {
        // Test without wraparound
        BeerTrackerGame g = new BeerTrackerGame(30, 15, 5, false);

        Set<Integer> expectedSimpleResults = new HashSet<>();
        Integer[] simpleElements = {0, 1, 2, 3, 4};
        Collections.addAll(expectedSimpleResults, simpleElements);

        assertEquals(expectedSimpleResults, g.getCellsOccupiedByObject(0, 5));

        // Test with wraparound
        Set<Integer> expectedAdvancedResults = new HashSet<>();
        Integer[] advancedElements = {0, 1, 2, 28, 29};
        Collections.addAll(expectedAdvancedResults, advancedElements);

        assertEquals(expectedAdvancedResults, g.getCellsOccupiedByObject(28, 5));
    }
}