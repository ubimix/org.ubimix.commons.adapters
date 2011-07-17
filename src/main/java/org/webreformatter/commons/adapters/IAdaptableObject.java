package org.webreformatter.commons.adapters;

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
    <T extends IAdapter> T getAdapter(Class<T> type);

    /**
     * This method is used to notify all registered (and instantiated) adapters
     * about an event.
     * 
     * @param event - the fired event
     */
    void notifyAdapters(IAdapterEvent event);

}