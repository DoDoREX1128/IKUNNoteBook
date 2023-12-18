package com.example.tetris; // 声明包名

import org.junit.Test; // 导入JUnit的Test类

import static org.junit.Assert.*; // 导入JUnit的断言类

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test // 声明一个测试方法
    public void addition_isCorrect() { // 方法名：addition_isCorrect
        assertEquals(4, 2 + 2); // 使用断言方法assertEquals判断2 + 2的结果是否等于4
    }
}