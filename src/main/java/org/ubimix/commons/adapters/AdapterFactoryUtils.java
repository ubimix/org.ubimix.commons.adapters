/**
 * 
 */
package org.ubimix.commons.adapters;

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
        final Class<?> adapterInterfaceType,
        final Class<?> adapterImplType) {
        IAdapterFactory factory = null;
        try {
            try {
                Method m = adapterImplType
                    .getDeclaredMethod("getAdapterFactory");
                factory = (IAdapterFactory) m.invoke(null);
            } catch (NoSuchMethodException e) {
            }
            if (factory == null) {
                Constructor<?>[] constructors = adapterImplType
                    .getConstructors();
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
                                if (!adapterInterfaceType
                                    .isAssignableFrom(type)
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
                            + adapterInterfaceType.getName()
                            + "'.",
                        null);
                }
            }
        } catch (Throwable t) {
            throw handleError("Can not register an adapter factory. Type: '"
                + adapterInterfaceType
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

    public static boolean isClass(Class<?> type) {
        return !type.isInterface()
            && !type.isArray()
            && !type.isAnnotation()
            && !type.isEnum()
            && !type.isAnonymousClass();
    }

    public static IAdapterFactory registerAdapter(
        IAdapterRegistry registry,
        Class<?> adaptableType,
        final Class<?> adapterType) {
        return registerAdapter(
            registry,
            adaptableType,
            adapterType,
            adapterType);
    }

    public static IAdapterFactory registerAdapter(
        IAdapterRegistry registry,
        Class<?> adaptableType,
        final Class<?> adapterInterfaceType,
        final Class<?> adapterImplType) {
        if (!isClass(adapterImplType)) {
            throw new IllegalArgumentException("The '"
                + adapterImplType.getName()
                + "' type should be a class.");
        }
        if (!adapterInterfaceType.isAssignableFrom(adapterImplType)) {
            throw new IllegalArgumentException("The '"
                + adapterImplType.getName()
                + "' adapter class should implement or extend the '"
                + adapterInterfaceType.getName()
                + "'.");
        }
        IAdapterFactory factory = getAdapterFactory(
            adaptableType,
            adapterInterfaceType,
            adapterImplType);
        registry.registerAdapterFactory(factory, adapterInterfaceType);
        return factory;
    }
}
