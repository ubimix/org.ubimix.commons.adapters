package org.ubimix.commons.adapters;

/**
 * The common superclass for all adaptable objects.
 * 
 * @author kotelnikov
 */
public class AdaptableObject extends AbstractAdaptableObject {

    private IAdapterFactory fFactory;

    public AdaptableObject(IAdapterFactory factory) {
        fFactory = factory;
    }

    @Override
    public IAdapterFactory getAdapterFactory() {
        return fFactory;
    }

}
