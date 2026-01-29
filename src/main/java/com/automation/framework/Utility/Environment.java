package com.automation.framework.Utility;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({ "classpath:${testenv}.properties" })
public interface Environment extends Config {

    @Key("Env")
    String Env();

    @Key("filepath")
    String filepath();

    @Key("downloadpath")
    String downloadpath();

    // Application URL
    @Key("url")
    String url();

    // Test Users (generic)
    @Key("testUser")
    String testUser();

    @Key("testPassword")
    String testPassword();

    @Key("adminUser")
    String adminUser();

    @Key("adminPassword")
    String adminPassword();
} 