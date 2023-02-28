package proj2;
/**
 * CSC 151: DATA STRUCTURES, PROJECT 2: GET IN LINE
 * @author Claudia Porto
 * @version 9/20/2022
 */
public class Sequence
{
    /**
     * Invariants of the Sequence class:
     *  1. If size > 0, the contents are stored in holder at indices 0 to size-1 and the contents at indices
     *     >= size are irrelevant.
     *  2. If size = 0, the contents are irrelevant.
     *  3. If there is no current index, current = -1
     *  4. 0 <= size <= holder.length
     *  5. -1 < current < size
     */

    private final int DEFAULT_CAPACITY = 10;
    private final int DEFAULT_CURRENT = -1;
    private final int EMPTY = 0;
    private final int START = 0;

    private int size;
    private int current;
    private String[] holder;

    /**
     * Creates a new sequence with initial capacity 10.
     */
    public Sequence() {
    	this.size = EMPTY;
        this.current = DEFAULT_CURRENT;
        this.holder = new String[DEFAULT_CAPACITY];
    }
    

    /**
     * Creates a new sequence.
     * 
     * @param initialCapacity the initial capacity of the sequence.
     */
    public Sequence(int initialCapacity){
    	this.size = EMPTY;
        this.current = DEFAULT_CURRENT;
        this.holder = new String[initialCapacity];
    }
    

    /**
     * Adds a string to the sequence in the location before the
     * current element. If the sequence has no current element, the
     * string is added to the beginning of the sequence.
     *
     * The added element becomes the current element.
     *
     * If the sequences's capacity has been reached, the sequence will
     * expand to twice its current capacity plus 1.
     *
     * @param value the string to add.
     */
    public void addBefore(String value)
    {
        capacityReached();

        if(isEmpty()){
            this.holder[START] = value;
            this.current = START;
        }else{
            if(!isCurrent()){
                setCurrent(START);
            }
            for(int i = size()-1; i >= getCurrentIndex(); i--){
                this.holder[i+1] = this.holder[i];
            }
            this.holder[getCurrentIndex()] = value;
        }

        this.size++;
    }
    
    
    /**
     * Adds a string to the sequence in the location after the current
     * element. If the sequence has no current element, the string is
     * added to the end of the sequence.
     *
     * The added element becomes the current element.
     *
     * If the sequences's capacity has been reached, the sequence will
     * expand to twice its current capacity plus 1.
     *
     * @param value the string to add.
     */
    public void addAfter(String value)
    {
        capacityReached();

        if(!isCurrent()) {
            this.holder[size()] = value;
            this.current = size();
        }else{
            for(int i = size(); i > getCurrentIndex(); i--){
                this.holder[i] = this.holder[i-1];
            }
            this.holder[getCurrentIndex() + 1] = value;
            setCurrent(getCurrentIndex()+1);
        }

        this.size++;
    }


    private void capacityReached(){
        if(size() == getCapacity()) {
            ensureCapacity((getCapacity() * 2) + 1);
        }
    }

    
    /**
     * @return true if and only if the sequence has a current element.
     */
    public boolean isCurrent()
    {
        return getCurrentIndex() != DEFAULT_CURRENT;
    }
    
    
    /**
     * @return the capacity of the sequence.
     */
    public int getCapacity()
    {
        return this.holder.length;
    }

    
    /**
     * @return the element at the current location in the sequence, or
     * null if there is no current element.
     */
    public String getCurrent()
    {
        if (isCurrent()){
            return this.holder[getCurrentIndex()];
        }else {
            return null;
        }
    }
    
    
    /**
     * Increase the sequence's capacity to be
     * at least minCapacity.  Does nothing
     * if current capacity is already >= minCapacity.
     *
     * @param minCapacity the minimum capacity that the sequence
     * should now have.
     */
    public void ensureCapacity(int minCapacity)
    {
        if(getCapacity() < minCapacity){
            String[] temp = new String[minCapacity];
            copyArrayItems(this.holder, temp);
            this.holder = temp;
        }
    }


    private void copyArrayItems(String[] original, String[] copy){
        for(int i = 0; i < original.length; i++){
            copy[i] = original[i];
        }
    }
    
    /**
     * Places the contents of another sequence at the end of this sequence.
     *
     * If adding all elements of the other sequence would exceed the
     * capacity of this sequence, the capacity is changed to make room for
     * all of the elements to be added.
     * 
     * Postcondition: NO SIDE EFFECTS!  the other sequence should be left
     * unchanged.  The current element of both sequences should remain
     * where they are. (When this method ends, the current element
     * should refer to the same element that it did at the time this method
     * started.)
     *
     * @param another the sequence whose contents should be added.
     */
    public void addAll(Sequence another)
    {
        if((this.size() + another.size()) > this.getCapacity()) {
            ensureCapacity((size() + another.size()));
        }

        int originalCurrent = this.getCurrentIndex();
        int anotherIndex = START;

        this.setCurrent(this.size()-1);
        int totalSize = this.size() + another.size();

        for(int i = size(); i < totalSize; i++){
            addAfter(another.holder[anotherIndex]);
            anotherIndex++;
        }

        this.setCurrent(originalCurrent);
    }


    /**
     * Move forward in the sequence so that the current element is now
     * the next element in the sequence.
     *
     * If the current element was already the end of the sequence,
     * then advancing causes there to be no current element.
     *
     * If there is no current element to begin with, do nothing.
     */
    public void advance()
    {
        if(isCurrent()) {
            if (lastCurrent()) {
                setCurrent(DEFAULT_CURRENT);
            }else {
                setCurrent(getCurrentIndex() + 1);
            }
        }
    }


    private boolean lastCurrent(){
        return getCurrentIndex() == (size() - 1);
    }

    /**
     * Make a copy of this sequence.  Subsequence changes to the copy
     * do not affect the current sequence, and vice versa.
     *
     * Postcondition: NO SIDE EFFECTS!  This sequence's current
     * element should remain unchanged.  The clone's current
     * element will correspond to the same place as in the original.
     *
     * @return the copy of this sequence.
     */
    public Sequence clone() {
        Sequence copySequence = new Sequence(getCapacity());
        copySequence.size = size();
        copySequence.current = getCurrentIndex();

        copyArrayItems(this.holder, copySequence.holder);

        return copySequence;
    }


    /**
     * Remove the current element from this sequence.  The following
     * element, if there was one, becomes the current element.  If
     * there was no following element (current was at the end of the
     * sequence), the sequence now has no current element.
     *
     * If there is no current element, does nothing.
     */
    public void removeCurrent()
    {
        if(isCurrent()){
            if(!lastCurrent()){
                for (int i = getCurrentIndex(); i < size(); i++) {
                    this.holder[i] = this.holder[i+1];
                }
            }else{
                setCurrent(DEFAULT_CURRENT);
            }
            this.size--;
        }
    }


    private void setCurrent(int newCurrent){
        this.current = newCurrent;
    }

    
    /**
     * @return the number of elements stored in the sequence.
     */
    public int size()
    {
        return this.size;
    }

    /**
     * @return the value of the current index
     */
    public int getCurrentIndex(){
        return this.current;
    }

    
    /**
     * Sets the current element to the start of the sequence.  If the
     * sequence is empty, the sequence has no current element.
     */
    public void start()
    {
        if(isEmpty()) {
            setCurrent(DEFAULT_CURRENT);
        }else{
            setCurrent(START);
        }
    }

    
    /**
     * Reduce the current capacity to its actual size, so that it has
     * capacity to store only the elements currently stored.
     */
    public void trimToSize()
    {
        Sequence smaller = new Sequence(this.size());
        if (getCapacity() > size()) {
            copyArrayItems(smaller.holder, this.holder);
            this.holder = smaller.holder;
        }
    }
    
    
    /**
     * Produce a string representation of this sequence.  The current
     * location is indicated by a >.  For example, a sequence with "A"
     * followed by "B", where "B" is the current element, and the
     * capacity is 5, would print as:
     * 
     *    {A, >B} (capacity = 5)
     * 
     * The string you create should be formatted like the above example,
     * with a comma following each element, no comma following the
     * last element, and all on a single line.  An empty sequence
     * should give back "{}" followed by its capacity.
     * 
     * @return a string representation of this sequence.
     */
    public String toString() 
    {
        String sequenceString = "{";

        if(!isEmpty()) {
            for (int i = 0; i < size(); i++) {
                if(i == getCurrentIndex()){
                    sequenceString += ">";
                    sequenceString += this.holder[i];
                    if(i+1 != size) {
                        sequenceString += ", ";
                    }
                }else {
                    sequenceString += this.holder[i];
                    if(i+1 != size) {
                        sequenceString += ", ";
                    }
                }
            }
        }

        sequenceString += "} ";

        String capacity = String.format("(capacity = %d)", getCapacity());
        return sequenceString + capacity;
    }
    
    /**
     * Checks whether another sequence is equal to this one.  To be
     * considered equal, the other sequence must have the same size
     * as this sequence, have the same elements, in the same
     * order, and with the same element marked
     * current.  The capacity can differ.
     * 
     * Postcondition: NO SIDE EFFECTS!  this sequence and the
     * other sequence should remain unchanged, including the
     * current element.
     * 
     * @param other the other Sequence with which to compare
     * @return true iff the other sequence is equal to this one.
     */
    public boolean equals(Sequence other) 
    {
        if((this.size() != other.size()) || (this.getCurrentIndex() != other.getCurrentIndex())){
            return false;
        }
        for(int i = 0; i < this.size(); i++){
            if(!(this.holder[i].equals(other.holder[i]))){
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @return true if Sequence empty, else false
     */
    public boolean isEmpty()
    {
        return size() == EMPTY;
    }
    
    
    /**
     *  empty the sequence. There should be no current element.
     */
    public void clear()
    {
        setCurrent(DEFAULT_CURRENT);
        this.size = EMPTY;
    }
}