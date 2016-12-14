package com.plugin.utils.comparator;

import com.plugin.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Description: 一个针对排序的事例
 * @Author: zxl
 * @Date: 2016/12/14 10:06
 */

public class ComparatorUtils {

    private static volatile ComparatorUtils mInstance = null;

    private ComparatorUtils() {
    }

    public static ComparatorUtils getInstance() {
        ComparatorUtils instance = mInstance;
        if (instance == null) {
            synchronized (ComparatorUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new ComparatorUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    /**
     * 针对List集合的排序，Collections.sort(List<T> list, Comparator<? super T> c)
     */
    public void testComparatorSort() {
        List<TestBean> testBeanList = new ArrayList<>();
        TestBeanComparator testBeanComparator = new TestBeanComparator();
        testBeanList.add(new TestBean(3));
        testBeanList.add(new TestBean(2));
        testBeanList.add(new TestBean(4));
        testBeanList.add(new TestBean(6));
        testBeanList.add(new TestBean(1));
        LogUtils.e("---------" + testBeanList.toString());
        Collections.sort(testBeanList, testBeanComparator);
        LogUtils.e("Comparator---------" + testBeanList.toString());
    }

    /**
     * 针对List集合的排序，Collections.sort(List<T> list)
     */
    public void testSort() {
        List<String> data = new ArrayList<>();
        data.add("A");
        data.add("F");
        data.add("E");
        data.add("G");
        data.add("H");
        LogUtils.e("---------" + data.toString());
        Collections.sort(data);
        LogUtils.e("Comparator---------" + data.toString());
    }

    class TestBeanComparator implements Comparator<TestBean> {

        @Override
        public int compare(TestBean t1, TestBean t2) {
            if (t1.getId() == t2.getId()) {
                return 0;
            }
            return t1.getId() > t2.getId() ? 1 : -1;
        }
    }

    class TestBean {
        private int id;

        public TestBean(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return '{' + "id=" + id + '}';
        }
    }
}
