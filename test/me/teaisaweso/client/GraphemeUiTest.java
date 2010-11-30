package me.teaisaweso.client;

import java.io.File;

import junit.framework.TestCase;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

public class GraphemeUiTest extends TestCase {
    FirefoxDriver mFd = new FirefoxDriver(new FirefoxProfile(new File("foxprofiles/6bzydfod.default")));

    public void testBees() {
        
        mFd.navigate().to("http://127.0.0.1:8888/index.html?gwt.codesvr=127.0.0.1:9997");
    }
}
