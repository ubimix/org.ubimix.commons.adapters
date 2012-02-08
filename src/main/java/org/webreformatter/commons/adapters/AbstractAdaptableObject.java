package org.webreformatter.commons.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * The common superclass for all adaptable objects.
 * 
 * @author kotelnikov
 */
public abstract class AbstractAdaptableObject implements IAdaptableObject {

    private Map<Class<?>, Object> fAdapters = new WeakHashMap<Class<?>, Object>();

    public AbstractAdaptableObject() {
    }

    /**
     * @see org.webreformatter.commons.adapters.IAdaptableObject#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Class<T> type) {
        synchronized (fAdapters) {
            Object obj = fAdapters.get(type);
            if (obj == null || !(type.isInstance(obj))) {
                IAdapterFactory factory = getAdapterFactory();
                obj = factory != null ? factory.getAdapter(this, type) : null;
                if (obj != null) {
                    fAdapters.put(type, obj);
                }
            }
            return (T) obj;
        }
    }

    public abstract IAdapterFactory getAdapterFactory();

    /**
     * Returns a map of all adapters associated with this adaptable object.
     * 
     * @return a map of all adapters associated with this adaptable object
     */
    public Map<Class<?>, Object> getAdapters() {
        synchronized (fAdapters) {
            return new HashMap<Class<?>, Object>(fAdapters);
        }
    }

}
