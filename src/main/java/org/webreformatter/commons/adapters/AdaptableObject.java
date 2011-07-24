package org.webreformatter.commons.adapters;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * The common superclass for all adaptable objects.
 * 
 * @author kotelnikov
 */
public class AdaptableObject implements IAdaptableObject {

    private Map<String, Object> fAdapters = new WeakHashMap<String, Object>();

    private IAdapterFactory fFactory;

    public AdaptableObject(IAdapterFactory factory) {
        fFactory = factory;
    }

    /**
     * @see org.webreformatter.commons.adapters.IAdaptableObject#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Class<T> type) {
        synchronized (fAdapters) {
            String name = type.getName();
            Object obj = fAdapters.get(name);
            if (obj == null || !(type.isInstance(obj))) {
                obj = fFactory.getAdapter(this, type);
                if (obj != null) {
                    fAdapters.put(name, obj);
                }
            }
            return (T) obj;
        }
    }

    public IAdapterFactory getAdapterFactory() {
        return fFactory;
    }

}
