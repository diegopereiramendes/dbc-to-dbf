package com.dbctodbf;

import com.dbctodbf.enuns.PlatformSystem;
import com.dbctodbf.interfaces.CLibrary;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class CBlastLibrary {

    private static final String NAME_TEMP_FILE = "blast_lib.file";
    private static final String NAME_LIB_WINDOWS = "blast_decrypt.dll";
    private static final String NAME_LIB_LINUX = "blast_decrypt.so";
    private static final String FOLDER_FILES = "files";
    private static final String FOLDER_LIBRARIES = "libraries";
    private static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

    private static CLibrary cLibraryInstance = null;

    public static CLibrary createInstance() throws IOException {

        if (Objects.isNull(cLibraryInstance)) {
            final String pathLibrary = getPathLibrary();
            cLibraryInstance = (CLibrary) Native.loadLibrary(pathLibrary, CLibrary.class);
        }
        return cLibraryInstance;
    }

    private static String getPathLibrary() throws IOException {

        ClassLoader classLoader = CBlastLibrary.class.getClassLoader();
        final String pathFolderLibraries = FOLDER_FILES + File.separator + FOLDER_LIBRARIES + File.separator;
        final InputStream inputStream;

        if (Platform.isLinux()) {
            inputStream = classLoader.getResourceAsStream(pathFolderLibraries + PlatformSystem.LINUX + File.separator + NAME_LIB_LINUX);

        } else {
            inputStream = classLoader.getResourceAsStream(pathFolderLibraries + PlatformSystem.WINDOWS + File.separator + NAME_LIB_WINDOWS);
        }

        return createFileTemp(inputStream);
    }

    private static String createFileTemp(InputStream inputStream) throws IOException {
        File tempFile = new File(TEMP_DIRECTORY + File.separator + NAME_TEMP_FILE);
        FileUtils.copyInputStreamToFile(inputStream, tempFile);
        return tempFile.getCanonicalPath();
    }
}

