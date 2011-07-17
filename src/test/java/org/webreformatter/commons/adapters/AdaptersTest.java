/**
 * 
 */
package org.webreformatter.commons.adapters;

import junit.framework.TestCase;

import org.webreformatter.commons.adapters.AdaptableObject;
import org.webreformatter.commons.adapters.CompositeAdapterFactory;
import org.webreformatter.commons.adapters.IAdapterFactory;
import org.webreformatter.commons.adapters.ObjectAdapter;

/**
 * @author kotelnikov
 */
public class AdaptersTest extends TestCase {

    public static class MyAdapter extends ObjectAdapter {
        public static IAdapterFactory getAdapterFactory() {
            return new IAdapterFactory() {
                @SuppressWarnings("unchecked")
                public <T> T getAdapter(Object instance, Class<T> type) {
                    if (!(instance instanceof MyObject)
                        || type != MyAdapter.class) {
                        return null;
                    }
                    return (T) new MyAdapter((MyObject) instance);
                }
            };
        }

        private MyObject fObject;

        public MyAdapter(MyObject object) {
            super();
            fObject = object;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MyAdapter)) {
                return false;
            }
            MyAdapter o = (MyAdapter) obj;
            return fObject.equals(o.fObject);
        }

        public MyObject getObject() {
            return fObject;
        }
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

    public void test() throws Exception {
        CompositeAdapterFactory factory = new CompositeAdapterFactory();
        factory.registerAdapterFactory(
            MyAdapter.getAdapterFactory(),
            MyAdapter.class);

        MyObject object = new MyObject(factory);
        MyAdapter adapter = object.getAdapter(MyAdapter.class);
        assertNotNull(adapter);
        assertSame(object, adapter.getObject());

        MyAdapter testAdapter = object.getAdapter(MyAdapter.class);
        assertSame(adapter, testAdapter);

        testAdapter = factory.getAdapter(object, MyAdapter.class);
        assertEquals(adapter, testAdapter);
    }
}
