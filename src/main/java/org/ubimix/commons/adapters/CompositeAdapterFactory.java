/**
 * 
 */
package org.ubimix.commons.adapters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class CompositeAdapterFactory
    implements
    IAdapterFactory,
    IAdapterRegistry {

    private Map<Class<?>, IAdapterFactory> fMap = new HashMap<Class<?>, IAdapterFactory>();

    public <T> T getAdapter(Object instance, Class<T> type) {
        IAdapterFactory factory = getAdapterFactory(type);
        if (factory == null) {
            return null;
        }
        T result = factory.getAdapter(instance, type);
        return result;
    }

    public IAdapterFactory getAdapterFactory(Class<?> type) {
        return fMap.get(type);
    }

    public synchronized void registerAdapterFactory(
        IAdapterFactory factory,
        Class<?>... types) {
        Map<Class<?>, IAdapterFactory> map = new HashMap<Class<?>, IAdapterFactory>(
            fMap);
        for (Class<?> type : types) {
            map.put(type, factory);
        }
        fMap = map;
    }

    public synchronized void removeAdapterFactory(
        IAdapterFactory factory,
        Class<?>... types) {
        Map<Class<?>, IAdapterFactory> map = new HashMap<Class<?>, IAdapterFactory>(
            fMap);
        for (Class<?> type : types) {
            map.remove(type);
        }
        fMap = map;
    }

}
