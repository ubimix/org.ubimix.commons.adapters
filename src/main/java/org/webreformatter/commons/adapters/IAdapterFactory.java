/**
 * 
 */
package org.webreformatter.commons.adapters;

/**
 * @author kotelnikov
 */
public interface IAdapterFactory {

    <T> T getAdapter(Object instance, Class<T> type);
}