/**
 * 
 */
package org.webreformatter.commons.adapters;

/**
 * This is a marker interface used to define adapters.
 * 
 * @author kotelnikov
 */
public interface IAdapter {
    void handleEvent(IAdapterEvent event);
}
