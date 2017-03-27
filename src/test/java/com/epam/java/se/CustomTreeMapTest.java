package com.epam.java.se;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import java.util.Map;
import java.util.stream.IntStream;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Мария on 26.03.2017.
 */
@FixMethodOrder
public class CustomTreeMapTest {

    private Map<Integer, String> m;

    @Before
    public void init() {
        m = new CustomTreeMap<>();
    }

    @Test
    public void testThatWeCanCreate() {
        assertThat(m, is(notNullValue()));
    }

    @Test
    public void testThatNewMapIsEmpty() {
        assertThat(m.isEmpty(), is(true));
    }

    @Test
    public void testThatNotNewMapIsNotEmpty() {
        m.put(10, "abc");
        m.put(20, "rtr");

        assertThat(m.isEmpty(), is(false));
    }

    @Test
    public void testThatOnNewMapContainKeyMethodReturnFalseForAnyObject() {
        assertThat(m.containsKey(new Integer(1)), is(false));
    }

    @Test
    public void testThatWeCanPutKeyValuePairAndCanCheckIt() {
        m.put(new Integer(3), "abc");
        m.put(new Integer(5), "rtr");
        assertThat(m.containsKey(3), is(true));
        assertThat(m.containsKey(5), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void testThatWeCantPutNullKey() {
        m.put(null, "abc");
    }

    @Test
    public void testThatWeCanPutNullValue() {
        m.put(1, null);
        assertThat(m.containsKey(1), is(true));
    }

    @Test
    public void testThatMapCanPutPairWithKeyThatAlreadyPresented() {

        String oldValue = "aaaa";
        String newValue = "bbbb";

        m.put(1, oldValue);
        m.put(1, newValue);

        assertFalse(m.containsValue(oldValue));
        assertTrue(m.containsValue(newValue));
    }

    @Test(expected = NullPointerException.class)
    public void testThatContainsKeyMethodThrowsExceptionOnNullKey() {
        m.containsKey(null);
    }

    @Test(expected = ClassCastException.class)
    public void testThatContainsKeyMethodThrowsExceptionOnWrongKeyClass() {
        m.put(1, ""); //TODO need to remove
        m.containsKey(new String(""));
    }

    @Test
    public void testContainsValueMethodWorksProperlyOn() {
        String value = "aaaa";

        m.put(1, value);

        assertTrue(m.containsValue(value));
    }

    @Test
    public void testContainsValueMethodWorksProperlyOnNullInputValue() {
        String value = "aaaa";

        m.put(1, value);

        assertFalse(m.containsValue(null));
    }

    @Test
    public void testThatWeCanPut10DifferentKeysInMap() {
        IntStream.range(1, 10).forEach(
                i -> m.put(i, String.valueOf(i))
        );

        IntStream.range(1, 10).forEach(
                i -> assertTrue(m.containsKey(i))
        );
    }

    @Test
    public void testThatMapCalculateItsSizeProperly() {
        m.put(new Integer(6), "abc");
        m.put(new Integer(8), "ferf");
        m.put(new Integer(9), "wef");

        assertThat(m.size(), is(3));
    }
    @Test
    public void testContainsValueMethodCanSearchMultipleValues() {
        String value = "aaaa";
        String value2 = "bbbb";
        String value3 = "ccccc";

        m.put(1, value);
        m.put(2, value2);
        m.put(3, value3);

        assertTrue(m.containsValue(value));
        assertTrue(m.containsValue(value2));
        assertTrue(m.containsValue(value3));
    }

    @Test
    public void testContainsValueMethodDoesntContainValueThatIsNotPresent() {
        String value = "aaaa";
        String value2 = "ffff";
        m.put(1, value);

        assertThat(m.containsValue(value2), is(false));
    }

    @Test
    public void testValueContainsMethodReturnsFalseOnWrongInputValueClass() {
        String value = "aaaa";
        m.put(1, value);

        assertThat(m.containsValue(222), is(false));
    }

    @Test
    public void testContainsKeyMethodDoesntContainValueThatIsNotPresent() {
        String value = "aaaa";
        m.put(1, value);

        System.out.println(m.containsKey(222));
    }

    @Test
    public void testThatGetMethodCanReturnValueByKey() {
        String value = "aaaa";
        String value2 = "bbbb";

        m.put(1, value);
        m.put(2, value2);

        assertThat(m.get(2), is(value2));
    }

    @Test(expected = NullPointerException.class)
    public void testThatGetMethodThrowsExceptionOnNullKey(){
        m.get(null);
    }


    @Test
    public void testThatWeCanRemoveByKey() {
        m.put(1, "aaa");
        m.put(2, "ccc");
        m.put(12, "eee");
        m.put(6, "ddd");
        m.put(1, "vvv");

        m.remove(12);
        m.remove(6);

        assertThat(m.containsKey(12), is(false));
        assertThat(m.containsKey(6), is(false));
        assertThat(m.containsKey(2), is(true));
    }

    @Test (expected = NullPointerException.class)
    public void testRemoveWithNullKey() {
        m.remove(null);
    }

    @Test
    public void testThatAfterRemoveSizeWillChange() {
        m.put(12, "eee");
        m.put(6, "ddd");
        m.put(1, "vvv");
        m.put(20, "vvv");

        m.remove(12);
        m.remove(6);

        assertThat(m.size(), is(2));
    }

    @Test
    public void testRemoveUnexistentKeyReturnsNull() {
        assertEquals(m.remove(12), null);
    }

    @Test
    public void testThatWeCanClearEntireMap() {

    }
}