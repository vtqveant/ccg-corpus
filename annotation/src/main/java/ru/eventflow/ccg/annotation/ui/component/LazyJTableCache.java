package ru.eventflow.ccg.annotation.ui.component;

/**
 * Class that is responsible for retrieving the data for the table from
 * the server and storing it locally.
 *
 * @author Jeremy Dickson, 2003.
 */

public class LazyJTableCache {

    // actual number of rows in the backing table
    private int rowCount;

    private int maximumCacheSize = -1;
    private int chunkSize = -1;
    private Object[] data = null;

    //AN INDEX- AN INTS ARE STORED CORREPONDING TO A ROWS REAL INDEX IN THE TABLE. THE LOCATION OF THE INDEX IN THIS
    //ARRAY SHOWS WHICH LOCATION TO ACCESS IN THE data ARRAY
    private int[] rowIndexLookup = null;

    //STORES THE INDEX THAT THE NEXT WRITES TO THE TWO ARRAYS SHOULD TAKE PLACE IN. WHEN IT REACHES
    //THE MAX CACHE SIZE IT GOES BACK TO ZERO
    private int writePositionIndex = 0;

    private LazyJTableDataSource tableDataSource = null;

    //THE LAST INDEX THAT WAS REQUIRED WHEN A FETCH OCCURRED. DETERMINES WHETHER THE USER IS ASCENDING
    //OR DESCENDING THE TABLE
    private int lastRequiredFetchRowIndex = 0;

    //THE LAST ARRAY INDEX OF THE CACHE TO BE INDEXED
    private int lastRowAccess = 0;

    public LazyJTableCache(int rowCount, LazyJTableDataSource tableDataSource) {
        this(rowCount, tableDataSource, 50, 1000);
    }

    /**
     * @param tableDataSource  A source of table data, (via the method <code>retrieveRows</code>).
     * @param chunkSize        The number of rows of data that are to be
     *                         retrieved from the remote store at a time.
     * @param maximumCacheSize The maximum number of rows that will be cached. When this number is exceeded
     *                         by new data that has been fetched, the oldest data is overwritten.
     */
    private LazyJTableCache(int rowCount, LazyJTableDataSource tableDataSource, int chunkSize, int maximumCacheSize) {
        this.rowCount = rowCount;
        this.tableDataSource = tableDataSource;

        if (chunkSize < 50) {
            chunkSize = 50;
        }
        this.chunkSize = chunkSize;

        if (maximumCacheSize < 300) {
            maximumCacheSize = 300;
        }
        this.maximumCacheSize = maximumCacheSize;

        if (this.chunkSize > maximumCacheSize) {
            this.chunkSize = maximumCacheSize;
        }

        data = new Object[maximumCacheSize];
        rowIndexLookup = new int[maximumCacheSize];

        //SET ALL THE ROWS TO -1, (THEY INITIALIZE TO 0).
        for (int i = 0; i < rowIndexLookup.length; i++) {
            rowIndexLookup[i] = -1;
        }
    }

    /**
     * Retrieves a row from the data cache. If the row is not currently in
     * the cache it will be retrieved from the DistributedTableDataSource object.
     *
     * @param rowIndex The row index in the table that is to be retrieved.
     */
    public Object[] retrieveRowFromCache(int rowIndex) {
        ensureRowCached(rowIndex);
        return (Object[]) data[getIndexOfRowInCache(rowIndex)];
    }

    /**
     * Invalidates the cache
     */
    public void invalidate() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
            rowIndexLookup[i] = -1;
        }
    }

    /**
     * Ensures that a row index in the table is cached and if not a chunk of data is retrieved
     */
    private void ensureRowCached(int rowIndex) {
        if (!isRowCached(rowIndex)) {
            //HAVE TO FETCH DATA FROM THE REMOTE STORE

            int fromIndex;
            int toIndex;

            if (rowIndex >= lastRequiredFetchRowIndex) {
                // the user is descending the table
                fromIndex = rowIndex;
                toIndex = rowIndex + chunkSize;

                try {
                    if (toIndex > rowCount) {
                        toIndex = rowCount;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // the user is ascending the table
                fromIndex = rowIndex - chunkSize;
                if (fromIndex < 0) {
                    fromIndex = 0;
                }
                toIndex = rowIndex + 1;
            }

            //RETRIEVE THE DATA
            Object[][] rows;
            try {
                rows = tableDataSource.retrieveRows(fromIndex, toIndex);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Problem occurred retrieving table data \n");
            }

            //ADD THE DATA TO THE CACHE
            for (int i = 0; i < rows.length; i++) {
                //SET THE VALUE IN THE DATA ARRAY
                data[writePositionIndex] = rows[i];

                //CREATE AN INDEX TO THE NEW CACHED DATA
                rowIndexLookup[writePositionIndex] = fromIndex + i;

                //CLOCK UP writePositionIndex AND REZERO IF NECESSARY
                if (writePositionIndex == (maximumCacheSize - 1)) {
                    writePositionIndex = 0;
                } else {
                    writePositionIndex++;
                }
                lastRequiredFetchRowIndex = rowIndex;
            }
        }
    }

    /**
     * Returns whether a particular row index in the table is cached.
     */
    private boolean isRowCached(int rowIndexInTable) {
        return getIndexOfRowInCache(rowIndexInTable) >= 0;
    }

    /**
     * Returns the array index of a particular row index in the table
     */
    private int getIndexOfRowInCache(int rowIndex) {
        for (int i = lastRowAccess; i < rowIndexLookup.length; i++) {
            if (rowIndexLookup[i] == rowIndex) {
                lastRowAccess = i;
                return i;
            }
        }
        for (int i = 0; i < lastRowAccess; i++) {
            if (rowIndexLookup[i] == rowIndex) {
                lastRowAccess = i;
                return i;
            }
        }
        return -1;
    }

}
