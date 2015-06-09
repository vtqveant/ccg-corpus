package ru.eventflow.ccg.annotation.ui.component;

public class LazyJTableCache {

    private static final int CHUNK_SIZE = 20;
    private static final int MAX_CACHE_CAPACITY = 1000;

    private int totalRowCount;
    private Object[] data = null;

    private int maxCacheSize = -1;
    private int chunkSize = -1;
    private int[] rowIndexLookup = null;
    private int writePositionIndex = 0;
    private int lastRequiredFetchRowIndex = 0;
    private int lastRowAccess = 0;

    private LazyJTableDataSource tableDataSource = null;

    public LazyJTableCache(int totalRowCount, LazyJTableDataSource tableDataSource) {
        this(totalRowCount, tableDataSource, CHUNK_SIZE, MAX_CACHE_CAPACITY);
    }

    /**
     * @param tableDataSource  A source of table data, (via the method <code>retrieveRows</code>).
     * @param chunkSize        The number of rows of data that are to be
     *                         retrieved from the remote store at a time.
     * @param maxCacheCapacity The maximum number of rows that will be cached. When this number is exceeded
     *                         by new data that has been fetched, the oldest data is overwritten.
     */
    private LazyJTableCache(int totalRowCount, LazyJTableDataSource tableDataSource, int chunkSize, int maxCacheCapacity) {
        this.totalRowCount = totalRowCount;
        this.tableDataSource = tableDataSource;

        this.maxCacheSize = maxCacheCapacity;
        this.chunkSize = chunkSize;
        if (this.chunkSize > maxCacheCapacity) {
            this.chunkSize = maxCacheCapacity;
        }

        data = new Object[maxCacheCapacity];
        rowIndexLookup = new int[maxCacheCapacity];

        // initalize rows index
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
            int fromIndex;
            int toIndex;

            if (rowIndex >= lastRequiredFetchRowIndex) {
                // the user is descending the table
                fromIndex = rowIndex;
                toIndex = rowIndex + chunkSize;

                try {
                    if (toIndex > totalRowCount) {
                        toIndex = totalRowCount;
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

            // retrieve the data
            Object[][] rows;
            try {
                rows = tableDataSource.retrieveRows(fromIndex, toIndex);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Problem occurred retrieving table data\n");
            }

            // cache the data
            for (int i = 0; i < rows.length; i++) {
                data[writePositionIndex] = rows[i];
                rowIndexLookup[writePositionIndex] = fromIndex + i;
                if (writePositionIndex == (maxCacheSize - 1)) {
                    writePositionIndex = 0;
                } else {
                    writePositionIndex++;
                }
                lastRequiredFetchRowIndex = rowIndex;
            }
        }
    }

    /**
     * Returns whether a particular row index in the table is cached
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
