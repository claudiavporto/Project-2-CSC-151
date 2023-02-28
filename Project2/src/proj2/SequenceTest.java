package proj2;
/**
 * JUnit test class.
 */
import org.junit.*;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

public class SequenceTest {

    @Rule //a test will fail if it takes longer than 1/10 of a second to run
    public Timeout timeout = Timeout.millis(100);

    /**
     * Creates a new sequence (default with an initial capacity of 10).
     * The final element added to the Sequence is the new current.
     * @param items the array of String values to be added
     * @return the new sequence
     */
    private Sequence makeSequence(String[] items){
        Sequence s = new Sequence();
        for(int i = 0; i < items.length; i++){
            s.addBefore(items[i]);
        }
        return s;
    }

    /**
     * Creates a new sequence (non-default with a given initial capacity).
     * The final element added to the Sequence is the new current.
     * @param items the array of String values to be added
     * @return the new sequence
     */
    private Sequence makeSequence(String[] items, int initialCapacity){
        Sequence s = new Sequence(initialCapacity);
        for(int i = 0; i < items.length; i++){
            s.addBefore(items[i]);
        }
        return s;
    }

    @Test //Sequence should be empty upon construction with capacity 10
    public void testConstructDefault(){
        Sequence s = new Sequence();
        assertEquals(0, s.size());
        assertEquals(10, s.getCapacity());
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //Sequence should be empty upon construction with given capacity (25)
    public void testConstructNonDefault(){
        Sequence s = new Sequence(25);
        assertEquals(0, s.size());
        assertEquals(25, s.getCapacity());
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //should reset instance variables to original default values
    public void testClear(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertEquals(0, s.getCurrentIndex());
        assertEquals(5, s.size());
        s.clear();
        assertEquals(-1, s.getCurrentIndex());
        assertEquals(0, s.size());
    }

    @Test //should return false because there is no current
    public void testIsCurrentEmpty(){
        Sequence s = new Sequence();
        assertFalse(s.isCurrent());
    }

    @Test //should return true because there is a current
    public void testIsCurrentNonEmpty(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertTrue(s.isCurrent());
    }

    @Test //should return true because current created after addBefore
    public void testIsCurrentAddBefore(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        s.addBefore("A");
        assertTrue(s.isCurrent());
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //should return true because current created after addAfter
    public void testIsCurrentAddAfter(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        s.addAfter("A");
        assertTrue(s.isCurrent());
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //should return false because no current when advanced at end of sequence
    public void testIsCurrentAdvance(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertEquals(0, s.getCurrentIndex());
        for(int i = 0; i < 5; i++) {
            s.advance();
        }
        assertFalse(s.isCurrent());
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //size should increment by 1 and current should become 0
    public void testAddBeforeNoCurrent(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        assertEquals(0, s.size());
        s.addBefore("A");
        assertEquals(0, s.getCurrentIndex());
        assertEquals("A", s.getCurrent());
        assertEquals(1, s.size());
    }

    @Test //size should increment by 1, sequence should be filled to capacity
    public void testAddBeforeOneSpaceLeft(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 6);
        assertEquals(5, s.size());
        s.addBefore("F");
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(0, s.getCurrentIndex());
        assertEquals(s.size(), s.getCapacity());
    }

    @Test //size should increment by 1
    public void testAddBeforeHasRoom(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 20);
        assertEquals(5, s.size());
        s.addBefore("F");
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //size should increment by 1, capacity should increase to double size plus 1
    public void testAddBeforeFull(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 5);
        assertEquals(5, s.size());
        s.addBefore("F");
        assertEquals(11, s.getCapacity());
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //size should increment by 1, current should become 0
    public void testAddBeforeNoCurrentNonEmpty(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 5);
        assertEquals(5, s.size());
        advanceCurrent(s, 5);
        s.addBefore("F");
        assertEquals(0, s.getCurrentIndex());
        assertEquals(6, s.size());
    }

    @Test //size should increment by 1, current should become 0
    public void testAddAfterNoCurrent(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        assertEquals(0, s.size());
        s.addAfter("A");
        assertEquals(0, s.getCurrentIndex());
        assertEquals("A", s.getCurrent());
        assertEquals(1, s.size());
    }

    @Test //size and current increment by 1, sequence filled to capacity
    public void testAddAfterOneSpaceLeft(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 6);
        assertEquals(5, s.size());
        advanceCurrent(s, 3);
        s.addAfter("F");
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(4, s.getCurrentIndex());
        assertEquals(s.size(), s.getCapacity());
    }

    @Test //size and current increment by 1
    public void testAddAfterHasRoom(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 20);
        assertEquals(5, s.size());
        advanceCurrent(s, 4);
        s.addAfter("F");
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(5, s.getCurrentIndex());
    }

    @Test //size and current increment by 1, capacity increase to double size plus 1
    public void testAddAfterFull(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 5);
        assertEquals(5, s.size());
        advanceCurrent(s, 4);
        s.addAfter("F");
        assertEquals(11, s.getCapacity());
        assertEquals(6, s.size());
        assertEquals("F", s.getCurrent());
        assertEquals(5, s.getCurrentIndex());
    }

    @Test
    public void testToStringDefaultEmpty(){
        Sequence s = new Sequence();
        assertEquals("{} (capacity = 10)", s.toString());
    }

    @Test
    public void testToStringDefaultNonEmpty(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertEquals("{>E, D, C, B, A} (capacity = 10)", s.toString());
    }

    @Test
    public void testToStringNonDefaultEmpty(){
        Sequence s = new Sequence(20);
        assertEquals("{} (capacity = 20)", s.toString());
    }

    @Test
    public void testToStringNonDefaultNonEmpty(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 20);
        advanceCurrent(s, 2);
        assertEquals("{E, D, >C, B, A} (capacity = 20)", s.toString());
    }


    @Test //should return the capacity of the sequence
    public void testGetCapacity(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 23);
        assertEquals(23, s.getCapacity());
    }

    @Test //should return the string at the current position in the sequence
    public void testGetCurrent(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items, 20);
        advanceCurrent(s, 2);
        assertEquals("C", s.getCurrent());
    }

    @Test //returns null since no current
    public void testGetCurrentNone(){
        Sequence s = new Sequence();
        assertNull(s.getCurrent());
    }

    @Test //capacity requested is less than original capacity, no change
    public void testEnsureCapacityReqLess(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertEquals(10, s.getCapacity());
        s.ensureCapacity(8);
        assertEquals(10, s.getCapacity());
    }

    @Test //capacity requested is greater than original capacity, increases to requested capacity
    public void testEnsureCapacityReqGreater(){
        String[] items = {"A", "B", "C", "D", "E"};
        Sequence s = makeSequence(items);
        assertEquals(10, s.getCapacity());
        s.ensureCapacity(26);
        assertEquals(26, s.getCapacity());
    }

    @Test //current becomes -1
    public void testAdvanceEnd(){
        String[] items = {"A", "B", "C"};
        Sequence s = makeSequence(items);
        assertEquals(0, s.getCurrentIndex());
        advanceCurrent(s, 3);
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //current increments by 1
    public void testAdvanceStart(){
        String[] items = {"A", "B", "C"};
        Sequence s = makeSequence(items);
        assertEquals(0, s.getCurrentIndex());
        s.advance();
        assertEquals(1, s.getCurrentIndex());
    }

    @Test //current increments by 2
    public void testAdvanceMiddle(){
        String[] items = {"A", "B", "C"};
        Sequence s = makeSequence(items);
        assertEquals(0, s.getCurrentIndex());
        advanceCurrent(s, 2);
        assertEquals(2, s.getCurrentIndex());
    }

    @Test //no change to current
    public void testAdvanceNoCurrent(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        s.advance();
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //equal but not same object
    public void testCloneEmpty(){
        Sequence s = new Sequence();
        Sequence s2 = s.clone();
        assertTrue(s.equals(s2));
        assertTrue(s2.equals(s));
        assertNotSame(s, s2);
    }

    @Test //equal but not same object
    public void testCloneNonEmpty(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        Sequence s2 = s.clone();
        assertTrue(s.equals(s2));
        assertTrue(s2.equals(s));
        assertNotSame(s, s2);
    }

    @Test //size 0, sequence is empty
    public void testSizeEmpty(){
        Sequence s = new Sequence();
        assertEquals(0, s.size());
    }

    @Test //returns size of sequence
    public void testSizeNonEmpty(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 20);
        assertEquals(3, s.size());
    }

    @Test //no change, no current
    public void testStartEmpty(){
        Sequence s = new Sequence();
        assertEquals(-1, s.getCurrentIndex());
        s.start();
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //current changed to 0
    public void testStartNonEmptyCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 20);
        s.advance();
        assertEquals(1, s.getCurrentIndex());
        s.start();
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //current changed to 0
    public void testStartNonEmptyNoCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 20);
        advanceCurrent(s, 3);
        assertEquals(-1, s.getCurrentIndex());
        s.start();
        assertEquals(0, s.getCurrentIndex());
    }

    @Test //capacity changed to 0, empty sequence
    public void testTrimToSizeEmpty(){
        Sequence s = new Sequence();
        assertEquals(0, s.size());
        assertEquals(10, s.getCapacity());
        s.trimToSize();
        assertEquals(0, s.getCapacity());
    }

    @Test //capacity changed to size, size<capacity
    public void testTrimToSizeGreaterCap(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        assertEquals(3, s.size());
        assertEquals(25, s.getCapacity());
        s.trimToSize();
        assertEquals(3, s.getCapacity());
    }

    @Test //returns true, sequence is empty
    public void testIsEmpty(){
        Sequence s = new Sequence();
        assertTrue(s.isEmpty());
    }

    @Test //returns false, sequence contains items
    public void testIsEmptyNonEmptyCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        assertFalse(s.isEmpty());
    }

    @Test //returns false, sequence contains items
    public void testIsEmptyNonEmptyNoCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        advanceCurrent(s, 3);
        assertFalse(s.isEmpty());
    }

    @Test //no change, empty sequence
    public void testClearEmpty(){
        Sequence s = new Sequence(30);
        assertEquals(30, s.getCapacity());
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
        s.clear();
        assertEquals(30, s.getCapacity());
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //changes size to 0 and current to -1, capacity does not change
    public void testClearNonEmpty(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        assertEquals(25, s.getCapacity());
        assertEquals(3, s.size());
        assertEquals(0, s.getCurrentIndex());
        s.clear();
        assertEquals(25, s.getCapacity());
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
    }

    @Test //no change, no current item to remove
    public void testRemoveCurrentEmpty(){
        Sequence s = new Sequence();
        assertEquals(0, s.size());
        s.removeCurrent();
        assertEquals(0, s.size());
    }

    @Test //no change, no current
    public void testRemoveCurrentNonEmptyNoCurrent(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        advanceCurrent(s, 3);
        assertEquals(-1, s.getCurrentIndex());
        assertEquals(3, s.size());
        s.removeCurrent();
        assertEquals(3, s.size());
    }

    @Test //size decreases by 1, current becomes -1 (end of sequence)
    public void testRemoveCurrentNonEmptyLastCurrent(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        advanceCurrent(s, 2);
        assertEquals(2, s.getCurrentIndex());
        assertEquals(3, s.size());
        s.removeCurrent();
        assertEquals(-1, s.getCurrentIndex());
        assertEquals(2, s.size());
        assertEquals("{A, B} (capacity = 25)", s.toString());
    }

    @Test //size decreases by 1, current doesn't change(first item)
    public void testRemoveCurrentNonEmptyFirstCurrent(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        assertEquals(0, s.getCurrentIndex());
        assertEquals(3, s.size());
        s.removeCurrent();
        assertEquals(0, s.getCurrentIndex());
        assertEquals(2, s.size());
        assertEquals("{>B, C} (capacity = 25)", s.toString());
    }

    @Test //size decreases by 1, current doesn't change
    public void testRemoveCurrentNonEmptyMiddleCurrent(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items, 25);
        s.advance();
        assertEquals(1, s.getCurrentIndex());
        assertEquals(3, s.size());
        s.removeCurrent();
        assertEquals(1, s.getCurrentIndex());
        assertEquals(2, s.size());
        assertEquals("{A, >C} (capacity = 25)", s.toString());
    }

    @Test //returns true, empties are equal
    public void testEqualsEmpties(){
        Sequence s = new Sequence();
        Sequence s2 = new Sequence();
        Sequence s3 = new Sequence(20);
        assertTrue(s.equals(s2));
        assertTrue(s2.equals(s));
        assertTrue(s.equals(s3));
        assertTrue(s.equals(s)); //reflexivity
        assertTrue(s3.equals(s)); //symmetry
    }

    @Test //returns false, one empty and one nonempty sequence (different sizes)
    public void testEqualsEmptyNonEmpty(){
        Sequence s = new Sequence();
        String[] items = {"C", "B", "A"};
        Sequence s2 = makeSequence(items, 25);
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns false, different sizes
    public void testEqualsDiffSize(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"D", "C", "B", "A"};
        Sequence s2 = makeSequence(items2);
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns false, different items
    public void testEqualsDiffItems(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"F", "E", "D"};
        Sequence s2 = makeSequence(items2);
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns false, one with and one without current
    public void testEqualsCurrNoCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"C", "B", "A"};
        Sequence s2 = makeSequence(items2);
        advanceCurrent(s2, 3);
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns false, different ordered items
    public void testEqualsDiffOrder(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"B", "A", "C"};
        Sequence s2 = makeSequence(items2);
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns false, different currents
    public void testEqualsDiffCurr(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"C", "B", "A"};
        Sequence s2 = makeSequence(items2);
        s2.advance();
        assertFalse(s.equals(s2));
        assertFalse(s2.equals(s));
    }

    @Test //returns true, both currents become -1
    public void testEqualsAdvanceToEnd(){
        String[] items = {"C", "B", "A"};
        Sequence s = makeSequence(items);
        advanceCurrent(s, 3);
        String[] items2 = {"C", "B", "A"};
        Sequence s2 = makeSequence(items2);
        advanceCurrent(s2, 3);
        assertTrue(s.equals(s2));
        assertTrue(s2.equals(s));
    }

    @Test //no change
    public void testAddAllEmptyToEmpty(){
        Sequence s = new Sequence();
        Sequence s2 = new Sequence();
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
        assertEquals("{} (capacity = 10)", s.toString());
    }

    @Test //size stays the same, current no change
    public void testAddAllEmptyToNonEmpty(){
        String[] items = {"B", "A"};
        Sequence s = makeSequence(items);
        Sequence s2 = new Sequence();
        assertEquals(2, s.size());
        assertEquals(0, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(2, s.size());
        assertEquals(0, s.getCurrentIndex());
        assertEquals("{>A, B} (capacity = 10)", s.toString());
        s.advance();
        assertEquals(2, s.size());
        assertEquals(1, s.getCurrentIndex());
        assertEquals("{A, >B} (capacity = 10)", s.toString());
    }

    @Test //size increase, current no change
    public void testAddAllNonEmptyToEmpty(){
        Sequence s = new Sequence();
        String[] items = {"B", "A"};
        Sequence s2 = makeSequence(items);
        assertEquals(0, s.size());
        assertEquals(-1, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(2, s.size());
        assertEquals(-1, s.getCurrentIndex());
        assertEquals("{A, B} (capacity = 10)", s.toString());
    }

    @Test //size increase, current no change
    public void testAddAllNonEmptyToNonEmpty(){
        String[] items = {"B", "A"};
        Sequence s = makeSequence(items);
        String[] items2 = {"E", "D", "C"};
        Sequence s2 = makeSequence(items2);
        s2.advance();
        assertEquals(2, s.size());
        assertEquals(0, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(5, s.size());
        assertEquals(0, s.getCurrentIndex());
        assertEquals("{>A, B, C, D, E} (capacity = 10)", s.toString());
    }

    @Test //size increase, capacity increase to fit all items, current no change
    public void testAddAllNonEmptyToNonEmptyCapReached(){
        String[] items = {"B", "A"};
        Sequence s = makeSequence(items, 4);
        String[] items2 = {"E", "D", "C"};
        Sequence s2 = makeSequence(items2);
        s2.advance();
        assertEquals(2, s.size());
        assertEquals(0, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(5, s.size());
        assertEquals(0, s.getCurrentIndex());
        assertEquals(5, s.getCapacity());
        assertEquals("{>A, B, C, D, E} (capacity = 5)", s.toString());
    }

    @Test //size increase, current no change
    public void testAddAllNonEmptyCurrToNonEmpty(){
        String[] items = {"B", "A"};
        Sequence s = makeSequence(items);
        advanceCurrent(s, 2);
        String[] items2 = {"C", "B", "A"};
        Sequence s2 = makeSequence(items2);
        s2.advance();
        assertEquals(2, s.size());
        assertEquals(-1, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(5, s.size());
        assertEquals(-1, s.getCurrentIndex());
        assertEquals("{A, B, A, B, C} (capacity = 10)", s.toString());
    }

    @Test //size increase, current no change
    public void testAddAllNonEmptyToNonEmptyCurr(){
        String[] items = {"D", "C", "B", "A"};
        Sequence s = makeSequence(items);
        advanceCurrent(s, 3);
        String[] items2 = {"A"};
        Sequence s2 = makeSequence(items2);
        s2.advance();
        assertEquals(4, s.size());
        assertEquals(3, s.getCurrentIndex());
        s.addAll(s2);
        assertEquals(5, s.size());
        assertEquals(3, s.getCurrentIndex());
        assertEquals("{A, B, C, >D, A} (capacity = 10)", s.toString());
    }


    /**
     * Advances the current of the sequence n times
     * @param s the sequence
     * @param times number of times to advance the current
     */
    private void advanceCurrent(Sequence s, int times){
        for(int i = 0; i < times; i++) {
            s.advance();
        }
    }
}
