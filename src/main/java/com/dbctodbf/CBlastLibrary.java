package com.dbctodbf;

import com.dbctodbf.enuns.PlatformSystem;
import com.dbctodbf.interfaces.CLibrary;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class CBlastLibrary {

    private static final String NOME_TEMP_DLL = "blast_decrypt.dll";
    private static final String NOME_TEMP_SO = "blast_decrypt.so";
    private static final String FOLDER_FILES = "files";
    private static final String FOLDER_LIBRARIES = "libraries";

    private static CLibrary cLibraryInstance = null;

    public static CLibrary createInstance() throws IOException {

        if (Objects.isNull(cLibraryInstance)) {
            final String pathLibrary = getPathLibrary();
            cLibraryInstance = (CLibrary) Native.loadLibrary(pathLibrary, CLibrary.class);
        }
        return cLibraryInstance;
    }

    private static String getPathLibrary() throws IOException {

        final String pathLibrary;
        final String pathFolderProject = new File(".").getCanonicalPath();
        final String pathFolderLibraries = pathFolderProject + File.separator + FOLDER_FILES + File.separator + FOLDER_LIBRARIES + File.separator;

        if (Platform.isLinux()) {
            pathLibrary = pathFolderLibraries + File.separator + PlatformSystem.LINUX + File.separator + NOME_TEMP_SO;
        } else {
            pathLibrary = pathFolderLibraries + File.separator + PlatformSystem.WINDOWS + File.separator + NOME_TEMP_DLL;
        }

        return pathLibrary;
    }
}

