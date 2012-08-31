/**
 * 
 */
package org.ubimix.commons.adapters;

/**
 * @author kotelnikov
 */
public interface IAdapterFactory {

    <T> T getAdapter(Object instance, Class<T> type);
}