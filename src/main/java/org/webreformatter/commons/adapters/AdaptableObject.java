package org.webreformatter.commons.adapters;

import java.util.Map;
import java.util.WeakHashMap;

public class AdaptableObject implements IAdaptableObject {

    private Map<String, IAdapter> fAdapters = new WeakHashMap<String, IAdapter>();

    private IAdapterFactory fFactory;

    public AdaptableObject(IAdapterFactory factory) {
        fFactory = factory;
    }

    /**
     * @see org.webreformatter.commons.adapters.IAdaptableObject#getAdapter(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public synchronized <T extends IAdapter> T getAdapter(Class<T> type) {
        String name = type.getName();
        IAdapter obj = fAdapters.get(name);
        if (obj == null || !(type.isInstance(obj))) {
            obj = fFactory.getAdapter(this, type);
            if (obj != null) {
                fAdapters.put(name, obj);
            }
        }
        return (T) obj;
    }

    public IAdapterFactory getAdapterFactory() {
        return fFactory;
    }

    /**
     * @see org.webreformatter.commons.adapters.IAdaptableObject#notifyAdapters(org.webreformatter.commons.adapters.IAdapterEvent)
     */
    public synchronized void notifyAdapters(IAdapterEvent event) {
        for (IAdapter adapter : fAdapters.values()) {
            adapter.handleEvent(event);
        }

    }
}
