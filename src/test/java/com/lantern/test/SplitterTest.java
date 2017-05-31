package com.lantern.test;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.List;

/**
 * Created by cat on 17-5-31.
 */
public class SplitterTest {
    @Test
    public void test() {
        String s = "1";
        List<String> slist = Splitter.on(",").splitToList(s);
        for(String s1 : slist) {
            System.out.println(s1);
        }
    }
}
