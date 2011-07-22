/**
 * 
 */
package org.webreformatter.commons.adapters;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class AdaptersTest extends TestCase {

    public static class Adapter1 {
        public static IAdapterFactory getAdapterFactory() {
            return new IAdapterFactory() {
                @SuppressWarnings("unchecked")
                public <T> T getAdapter(Object instance, Class<T> type) {
                    if (!(instance instanceof MyObject)
                        || type != Adapter1.class) {
                        return null;
                    }
                    return (T) new Adapter1((MyObject) instance);
                }
            };
        }

        private MyObject fObject;

        public Adapter1(MyObject object) {
            super();
            fObject = object;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Adapter1)) {
                return false;
            }
            Adapter1 o = (Adapter1) obj;
            return fObject.equals(o.fObject);
        }

        public MyObject getObject() {
            return fObject;
        }
    }

    public static class Adapter2 {
    }

    public static class MyObject extends AdaptableObject {
        public MyObject(IAdapterFactory factory) {
            super(factory);
        }
    }

    /**
     * @param name
     */
    public AdaptersTest(String name) {
        super(name);
    }

    public void test1() throws Exception {
        CompositeAdapterFactory factory = new CompositeAdapterFactory();
        factory.registerAdapterFactory(
            Adapter1.getAdapterFactory(),
            Adapter1.class);

        MyObject object = new MyObject(factory);
        Adapter1 adapter = object.getAdapter(Adapter1.class);
        assertNotNull(adapter);
        assertSame(object, adapter.getObject());

        Adapter1 testAdapter = object.getAdapter(Adapter1.class);
        assertSame(adapter, testAdapter);

        testAdapter = factory.getAdapter(object, Adapter1.class);
        assertEquals(adapter, testAdapter);
    }

    public void test2() {
        CompositeAdapterFactory factory = new CompositeAdapterFactory();
        AdapterFactoryUtils.registerAdapter(
            factory,
            MyObject.class,
            Adapter2.class);
        MyObject object = new MyObject(factory);
        Adapter2 adapter2 = object.getAdapter(Adapter2.class);
        assertNotNull(adapter2);
        assertSame(adapter2, object.getAdapter(Adapter2.class));

        assertNull(object.getAdapter(Adapter1.class));
        AdapterFactoryUtils.registerAdapter(
            factory,
            MyObject.class,
            Adapter1.class);
        Adapter1 adapter1 = object.getAdapter(Adapter1.class);
        assertNotNull(adapter1);
        assertSame(adapter1, object.getAdapter(Adapter1.class));
    }
}
