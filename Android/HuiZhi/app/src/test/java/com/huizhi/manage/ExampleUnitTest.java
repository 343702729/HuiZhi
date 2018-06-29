package com.huizhi.manage;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//        Log.i("HuiZhi", "The unit test");
        String str = "12321312";
        String[] strs = str.split(",");
        System.out.println("The strs length" + strs.length);
    }
}