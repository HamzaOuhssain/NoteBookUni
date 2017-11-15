package com.hamza.user.notebook;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private Context instrumentationCtx;
    private DBHelper mydb;


   /* @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.hamza.user.notebook", appContext.getPackageName());
    }*/
    @Before
    public void setUp(){
        mydb = new DBHelper(InstrumentationRegistry.getTargetContext());
        mydb.deleteAllElement();
        mydb.insertWord("ciao","salut", 0);
        mydb.insertWord("francia", "france", 0);
        mydb.insertWord("good","ca va", 0);
        mydb.insertWord("goodbye", "ourrovuoi", 0);
    }


 //Insert new word test
    @Test
    public void addWord_isCorrect() throws Exception {
        String b = mydb.getTraductionFromWord("ciao");
        assertEquals("salut", b);
        assertNotEquals("france", b);
    }

    //Uploading test
    @Test
    public void uploadWord_Test() throws Exception {
        mydb.updateContat("francia","frango", "francia");
        mydb.updateContat("goodmorning", "ourrovuoi", "goodbye");
        String b = mydb.getTraductionFromWord("francia");
        assertEquals("frango", b);
        b = mydb.getTraductionFromWord("goodmorning");
       assertEquals("ourrovuoi", b);
    }

    //Check the umber of the elements
    @Test
    public void numberOfWord_Test() throws Exception {
        int b = mydb.numberOfRows();
        assertEquals(4, b);
    }

    //Check if one word already exist in the database
    @Test
    public void checkAlreadyExist_Test() throws Exception {
        assertTrue(mydb.checkIsDataAlreadyInDBorNot("good"));
        assertFalse(mydb.checkIsDataAlreadyInDBorNot("Doesn't exist"));
    }


}
