package edu.umass.cs.ciir.controversy.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mhjang on 6/3/14.
 * It reads all files in a directory
 */
public class DirectoryManager {
    static int FILE_ONLY = 0, DIRECTORY_ONLY = 1;
    ArrayList<String> filenames;
    ArrayList<String> filePathList;
    int openOption = FILE_ONLY; // default
    String dir;

    public DirectoryManager(String dir_, int option) {

        final File folder = new File(dir);
        filenames = new ArrayList<String>();
        openOption = option;
        dir = dir_;

        readFiles(folder);
    }

    public DirectoryManager(String dir) {

        final File folder = new File(dir);
        filenames = new ArrayList<String>();
        System.out.println("reading " + folder.getAbsolutePath());
        this.dir = dir;
        readFiles(folder);

    }
    private void readFiles(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if(openOption == FILE_ONLY) {
                if (!fileEntry.getName().contains(".DS_Store") && !fileEntry.isDirectory()) {
                    filenames.add(fileEntry.getName());
                }
            }
            else {
                if (!fileEntry.getName().contains(".DS_Store")) {
                    filenames.add(fileEntry.getName());
                }
            }
        }
    }

    public static void makeDir(String dirName) {
        File theDir = new File(dirName);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            boolean result = false;

            try{
                theDir.mkdirs();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
        }
    }
    public ArrayList<String> getFileNameList() {
        return filenames;
    }

    /**
     * 7/
     * @return
     */
    public ArrayList<String> getFilePathList() {
       if(filePathList == null) {
           filePathList = new ArrayList<String>();
           for(int i=0; i<filenames.size(); i++) {
                filePathList.add(dir + "/" + filenames.get(i));
            }
        }
       return filePathList;
    }
}
