package com.dbctodbf.interfaces;

import com.sun.jna.Library;

public interface CLibrary extends Library {

    int dbc2dbf(String in, String out);
}


