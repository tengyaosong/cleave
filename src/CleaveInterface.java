package edu.gatech.seclass.cleave;

/**
 *  Interface created for use in Georgia Tech CS6300.
 *
 *  IMPORTANT: This interface should NOT be altered in any way.
 */
public interface CleaveInterface {

    /**
     * Reset the Cleave object to its initial state, for reuse.
     */
    void reset();

    /**
     * Sets the path of the input file. This method has to be called
     * before invoking the {@link processFile()} methods.
     *
     * @param filepath The file path to be set.
     */
    void setFilepath(String filepath);

    /**
     * Sets the field delimiter. If this method is not called,
     * the default value of the delimeter is the TAB symbol.
     *
     * @param ch Field delimiter to be used.
     */
    void setDelimiter(char ch);

    /**
     * Set Cleave's list of ranges, specified as a string that
     * contains comma-separated ranges.
     * It throws a {@link CleaveException} if the list is invalid.
     *
     * @param list Comma-separated list of ranges to be set.
     * @throws CleaveException
     */
    void setRangeList(String list) throws CleaveException;

    /**
     * Activates field processing mode of the cleave object.  After
     * setting, neither field nor character mode can be selected again
     * without first calling reset() on the object. If that happens,
     * the method throws a {@link CleaveException}.
     *
     * @throws CleaveException
    */
    void setFieldBasedProcessing() throws CleaveException;
    
    /**
     * Activates character processing mode of the cleave object.
     * After setting, neither field nor character mode can be selected
     * again without first calling reset() on the object. If that
     * happens, the method throws a {@link CleaveException}.
     *
     * @throws CleaveException
     */
    void setCharacterBasedProcessing() throws CleaveException;
    
    /**
     * Returns a System.lineSeperator() delimited string that contains
     * selected parts of the lines in the file specified using {@link setFilepath}
     * and according to the current configuration, which is set
     * through calls to the other methods in the interface.

     * It throws a {@link CleaveException} if an error condition
     * occurs (e.g., when the specified file does not exist).
     *
     * @return A System.lineSeperator() delimited string
     * @throws CleaveException
     */
    String processFile() throws CleaveException;
}
