package org.ubimix.commons.adapters;

/**
 * The common interface for all adapter objects.
 * 
 * @author kotelnikov
 */
public interface IAdaptableObject {

    /**
     * Returns an adapter of the specified type. If the adapter could not be
     * found then this method returns <code>null</code>.
     * 
     * @param <T>
     * @param type
     * @return an adapter of the specified type
     */
    <T> T getAdapter(Class<T> type);

}