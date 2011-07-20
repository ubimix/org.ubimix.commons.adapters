/**
 * 
 */
package org.webreformatter.commons.adapters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This utility class provides static methods used to dynamically generate
 * adapter factories and register them in {@link IAdapterRegistry}.
 * 
 * @author kotelnikov
 */
public class AdapterFactoryUtils {

    private final static Logger log = Logger
        .getLogger(AdapterFactoryUtils.class.getName());

    public static IAdapterFactory getAdapterFactory(
        final Class<?> adaptableType,
        final Class<?> adapterType) {
        IAdapterFactory factory = null;
        try {
            try {
                Method m = adapterType.getDeclaredMethod("getAdapterFactory");
                factory = (IAdapterFactory) m.invoke(null);
            } catch (NoSuchMethodException e) {
            }
            if (factory == null) {
                Constructor<?>[] constructors = adapterType.getConstructors();
                Constructor<?> resultConstructor = null;
                for (Constructor<?> constructor : constructors) {
                    Class<?>[] params = constructor.getParameterTypes();
                    if (params.length == 0
                        || (params.length == 1 && adaptableType
                            .isAssignableFrom(params[0]))) {
                        resultConstructor = constructor;
                        break;
                    }
                }
                if (resultConstructor != null) {
                    final Constructor<?> c = resultConstructor;
                    factory = new IAdapterFactory() {
                        @SuppressWarnings("unchecked")
                        public <T> T getAdapter(Object instance, Class<T> type) {
                            try {
                                if (!adapterType.isAssignableFrom(type)
                                    || !adaptableType.isInstance(instance)) {
                                    return null;
                                }
                                if (c.getParameterTypes().length == 0) {
                                    return (T) c.newInstance();
                                } else {
                                    return (T) c.newInstance(instance);
                                }
                            } catch (Exception t) {
                                handleError(
                                    "Can not create an adapter for the instance '"
                                        + instance
                                        + "'.",
                                    t);
                                return null;
                            }
                        }
                    };
                } else {
                    throw handleError(
                        "Can not create adapter factory for the type '"
                            + adaptableType.getName()
                            + ". Adapter type: "
                            + adapterType.getName()
                            + "'.",
                        null);
                }
            }
        } catch (Throwable t) {
            throw handleError("Can not register an adapter factory. Type: '"
                + adapterType
                + "'.", t);
        }
        return factory;
    }

    private static IllegalArgumentException handleError(String msg, Throwable e) {
        log.log(Level.FINE, msg, e);
        if (e instanceof IllegalArgumentException) {
            return (IllegalArgumentException) e;
        }
        throw new IllegalArgumentException(msg, e);
    }

    public static void registerAdapter(
        IAdapterRegistry registry,
        Class<?> adaptableType,
        final Class<?> adapterType) {
        IAdapterFactory factory = getAdapterFactory(adaptableType, adapterType);
        registry.registerAdapterFactory(factory, adapterType);
    }
}
