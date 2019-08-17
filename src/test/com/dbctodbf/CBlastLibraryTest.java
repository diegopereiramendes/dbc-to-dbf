package com.dbctodbf;

import com.dbctodbf.interfaces.CLibrary;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public class CBlastLibraryTest {

    private static final String FILE_IN_DBC = "PFAC1801.DBC";
    private static final String FILE_OUT_DBF = "PFAC1801.DBF";

    @Test
    public void dbc2dbf() {

        try {
            final String in = getPathFolderFiles() + FILE_IN_DBC;
            final String out = getPathFolderFiles() + FILE_OUT_DBF;
            final CLibrary cLib;
            cLib = CBlastLibrary.createInstance();
            cLib.dbc2dbf(in, out);
        } catch (IOException e) {
            fail("Houve um erro ao carregar a biblioteca: caminho inválido");
        }
    }

    private String getPathFolderFiles() throws IOException {

        String path = new File(".").getCanonicalPath();
        return path + File.separator + "files" + File.separator;
    }
}