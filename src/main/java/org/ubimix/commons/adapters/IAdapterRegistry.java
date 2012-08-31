/**
 * 
 */
package org.ubimix.commons.adapters;

/**
 * @author kotelnikov
 */
public interface IAdapterRegistry {

    IAdapterFactory getAdapterFactory(Class<?> type);

    void registerAdapterFactory(IAdapterFactory factory, Class<?>... types);

    void removeAdapterFactory(IAdapterFactory factory, Class<?>... types);

}
