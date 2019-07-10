/* File : JniWrapper.java */
package org.acme;

import java.nio.file.Paths;
 
public class JniWrapper {
    private static String OS = System.getProperty("os.name").toLowerCase();

    final private static String MAC_LIB_NAME = "target/libjnilibrary.dylib";
    final private static String NIX_LIB_NAME = "target/libjnilibrary.so";
    final private static String WIN_LIB_NAME = "target/jnilibrary.dll";
    private static boolean isInit = false;

    public JniWrapper() {
        init();
    }

    private static synchronized void init() {
        if(!isInit) {
            isInit = true;
            try {
                if (isMac()) {
                    System.load(Paths.get(MAC_LIB_NAME).toAbsolutePath().toString());
                } else if (isNix()) {
                    System.load(Paths.get(NIX_LIB_NAME).toAbsolutePath().toString());
                } else if (isWin()) {
                    System.load(Paths.get(WIN_LIB_NAME).toAbsolutePath().toString());
                } else {
                    System.err.println("System not reconized, cannot load the lib");
                }
            } catch(SecurityException | UnsatisfiedLinkError e) {
                e.printStackTrace();
            }
        }
    }

	public static boolean isWin() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isNix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}
 
    public native String getString();
}