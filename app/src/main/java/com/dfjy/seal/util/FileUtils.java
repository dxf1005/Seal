
package com.dfjy.seal.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileUtils {

    // LOG标志
    private static final String TAG = "FileUtils";
    private String rootDir = null;
    private final static int BUFFER = 1024;

    /**
     * 得到文件的扩展名
     * 
     * @param uri 文件名或者文件uri
     * @return 返回文件的扩展名，如果文件名是null则返回null 如果文件没有扩展名则返回空
     */
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            return "";
        }
    }

    /**
     * 得到文件的扩展名
     * 
     * @param uri 文件名或者文件uri
     * @return 返回文件的扩展名，如果文件名是null则返回null 如果文件没有扩展名则返回空
     */
    public static String getExtensionNoDot(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0 && uri.length() > dot + 1) {
            return uri.substring(dot + 1);
        } else {
            return "";
        }
    }

    /**
     * 把文件转化成uri
     * 
     * @param file 文件对象
     * @return uri结果
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * 把uri转化成文件
     * 
     * @param uri
     * @return file对象
     */
    public static File getFile(Uri uri) {
        if (uri != null) {
            String filepath = uri.getPath();
            if (filepath != null) {
                return new File(filepath);
            }
        }
        return null;
    }

    /**
     * 得到文件的路径，不包含名字
     * 
     * @param file
     * @return 文件名
     */
    public static File getPathWithoutFileName(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname = filepath.substring(0,
                        filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0,
                            pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * 得到文件的名字，不包含路径
     * 
     * @param file
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file != null && !file.isDirectory()) {
            String fileName = file.getName();

            String name = fileName.substring(0, fileName.length()
                    - getExtension(fileName).length());
            return name;
        }
        return null;
    }

    /**
     * 得到文件的名字，不包含路径
     * 
     * @param name 文件路径
     * @return 文件名
     */
    public static String getFileName(final String name) {
        String trueName = name;
        int index = name.lastIndexOf("/");
        if (index > -1) {
            trueName = trueName.substring(index + 1);
        }
        int extensionSize = getExtension(trueName).length();
        if (extensionSize > 0) {
            trueName = trueName.substring(0, trueName.length() - extensionSize);
        }
        return trueName;
    }

    /**
     * 得到文件的名字，包含路径
     * 
     * @param name 文件路径
     * @return 文件名
     */
    public static String getFileNameAndExtend(final String name) {
        String trueName = name;
        int index = name.lastIndexOf("/");
        if (index > -1) {
            trueName = trueName.substring(index + 1);
        }
        return trueName;
    }

    /**
     * 根据给定的路径与文件名字返回一个文件
     * 
     * @param curdir   文件路径
     * @param file   文件名字
     * @return 文件对象
     */
    public static File getFile(String curdir, String file) {
        String separator = "/";
        if (curdir.endsWith("/")) {
            separator = "/";
        }
        File clickedFile = new File(curdir + separator + file);
        return clickedFile;
    }

    /**
     * 根据给定的文件路径与文件名字返回一个文件
     * 
     * @param curdir 文件路径
     * @param file 文件名
     * @return 文件
     */
    public static File getFile(File curdir, String file) {
        return getFile(curdir.getAbsolutePath(), file);
    }

    /**
     * 根据给定的路径返回该路径下的所有文件
     * 
     * @return 路径名字
     */
    public static List<File> getFileList(String path) {
        File file = new File(path);
        ArrayList<File> dataList = new ArrayList<File>();
        if (file.exists() && file.canRead()) {
            if (file.isFile()) {
                dataList.add(file);
            } else if (file.isDirectory()) {
                String[] list = file.list();
                for (int i = 0; i < list.length; i++) {
                    dataList.addAll(getFileList(path + "/"
                            + list[i]));
                }
            }
        }
        return dataList;
    }

    /**
     * 创建文件夹
     * 
     * @param path 文件的路径
     * @param name 文件名字
     * @return
     */
    public static int createDir(String path, String name) {
        int len = path.length();

        if (len < 1 || len < 1)
            return -1;

        if (path.charAt(len - 1) != '/')
            path += "/";

        if (new File(path + name).exists())
            return 0;
        if (new File(path + name).mkdirs())
            return 0;

        return -1;
    }

    /**
     * 删除文件
     * 
     * @param path
     * @param name
     * @return
     */
    public static boolean deleteFile(String path, String name) {
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        if (path.charAt(len - 1) != '/')
            path += "/";
        File temFile = new File(path + name);

        if (temFile.exists()) {
            return temFile.delete();
        }

        return true;
    }

    /**
     * 删除文件
     * 
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        File temFile = new File(path);

        if (temFile.exists()) {
            return temFile.delete();
        }
        return true;
    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     * 
     * @param path 文件的路径
     * @return
     */
    public static boolean deleteFileOrDir(String path) {
        if (path == null)
            return false;
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        return deleteFileOrDir(new File(path));

    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     * 
     * @param temFile 文件对象
     * @return
     */
    public static boolean deleteFileOrDir(File temFile) {

        if (temFile == null) {
            return false;
        }

        if (!temFile.exists()) {
            return false;
        }
        if (temFile.isDirectory()) {
            File[] fileList = temFile.listFiles();
            int count = fileList.length;
            for (int i = 0; i < count; i++) {
                deleteFileOrDir(fileList[i]);
            }
            temFile.delete();
        } else if (temFile.isFile()) {
            temFile.delete();
        }
        return true;
    }

    /**
     * 删除文件或文件夹（包括文件夹下所有文件）
     * 
     * @param path 文件的路径
     * @return
     */
    public static boolean deleteDirContainFile(String path) {
        if (path == null) {
            return false;
        }
        return deleteDirContainFile(new File(path));
    }

    /**
     * 删除文件或文件夹（文件夹下所有文件）
     * 
     * @param file 文件的路径
     * @return
     */
    public static boolean deleteDirContainFile(File file) {
        if (file == null || !file.exists() || !file.isDirectory()) {
            return false;
        }

        File[] fileList = file.listFiles();
        int count = fileList.length;
        boolean flag = true;
        for (int i = 0; i < count; i++) {
            flag = flag && deleteFileOrDir(fileList[i]);
        }
        return flag;
    }

    /**
     * 根据跟定的文件路径进行创建文件
     * 
     * @param name 文件路径
     * @return -1失败 0成功
     */
    public static int createDir(String name) {
        int len = name.length();

        if (len < 1 || len < 1)
            return -1;

        if (new File(name).exists())
            return 0;
        if (new File(name).mkdir())
            return 0;

        return -1;
    }

    /**
     * list 比较对象
     */
    @SuppressWarnings("rawtypes")
    private static final Comparator alph = new Comparator<String>() {
        @Override
        public int compare(String arg0, String arg1) {
            return arg0.toLowerCase().compareTo(arg1.toLowerCase());
        }
    };

    /**
     * 该文件是否存在
     * 
     * @param name 文件路径
     * @return true存在 false不存在
     */
    public static boolean isExistFile(String name) {
        return new File(name).exists();
    }

    /**
     * 文件是否是文件夹
     * 
     * @param name 文件路径
     * @return
     */
    public static boolean isDirectoryPath(String name) {
        return new File(name).isDirectory();
    }

    /**
     * 得到文件根路径
     * 
     * @return
     */
    public String getRootDir() {
        return rootDir;
    }

    /**
     * 复制文件到指定目录
     * 
     * @param old_file 文件对象
     * @param newDir 将要复制到的文件夹的地址
     * @return 返回值0则成功，-1则失败
     */
    public int copyToDirectory(File old_file, String newDir, String fileName) {
        return copyToDirectory(old_file.getAbsolutePath(), newDir, fileName);
    }

    public static boolean copyAssetsFileToSdcard(Context context,
            String assName, String toDir, boolean isSkipExist) {
        BufferedInputStream its = null;
        FileOutputStream fots = null;
        try {

            File desfile = new File(toDir + "/" + assName);

            if (isSkipExist && desfile.exists()) {
                MyLog.d(TAG, "copyAssetsFileToSdcard false");
                return true;
            }
            if (desfile.exists()) {
                desfile.delete();
            }

            if (!desfile.getParentFile().exists()) {
                desfile.getParentFile().mkdirs();
            }
            if (!desfile.exists()) {
                desfile.createNewFile();
            }

            byte[] data = new byte[BUFFER];
            int read = 0;
            its = new BufferedInputStream(context
                    .getAssets().open(assName));

            fots = new FileOutputStream(desfile, true);

            while ((read = its.read(data, 0, BUFFER)) != -1) {
                fots.write(data, 0, read);
            }
            MyLog.d(TAG, "copyAssetsFileToSdcard true");
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            try {
                if (its != null) {
                    its.close();
                }
                if (fots != null) {
                    fots.close();
                }
                MyLog.d(TAG, "copyAssetsFileToSdcard end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制文件到指定目录
     * 
     * @param old 文件的路径地址或者文件夹地址
     * @param newDir 将要复制到的文件夹的地址
     * @return 返回值0则成功，-1则失败
     */
    public static int copyToDirectory(String old, String newDir, String fileName) {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read = 0;

        if (old_file.isFile() && temp_dir.isDirectory() && temp_dir.canWrite()) {
            File cp_file = null;
            if (fileName == null || fileName.equals("")) {
                String file_name = old.substring(
                        old.lastIndexOf("/"), old.length());
                cp_file = new File(newDir + file_name);
            } else {
                cp_file = new File(newDir + "/" + fileName);
            }

            BufferedOutputStream o_stream = null;
            BufferedInputStream i_stream = null;
            try {
                o_stream = new BufferedOutputStream(new FileOutputStream(
                        cp_file));
                i_stream = new BufferedInputStream(
                        new FileInputStream(old_file));

                while ((read = i_stream.read(data, 0, BUFFER)) != -1)
                    o_stream.write(data, 0, read);

                o_stream.flush();
                i_stream.close();
                o_stream.close();

            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
                return -1;

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return -1;
            } finally {
                if (i_stream != null) {
                    try {
                        i_stream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                if (o_stream != null) {
                    try {
                        o_stream.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

            }

        } else if (old_file.isDirectory() && temp_dir.isDirectory()
                && temp_dir.canWrite()) {
            String files[] = old_file.list();
            String dir = newDir
                    + old.substring(old.lastIndexOf("/"), old.length());
            int len = files.length;

            if (!new File(dir).mkdir())
                return -1;

            for (int i = 0; i < len; i++)
                copyToDirectory(old + "/" + files[i], dir, null);

        } else if (!temp_dir.canWrite()) {
            return -1;
        } else {
            return -1;
        }
        return 0;
    }

    /**
     * 获取当前可用的存储中，空间最大的存储的根目录。
     * 
     * @return
     */
    public static String getBiggestStorage() {
        String storageString = Environment.getExternalStorageDirectory()
                .getParent();
        File storageFile = new File(storageString);

        String biggestStorage = "";
        File[] files = storageFile.listFiles();
        long biggstSpace = 0;
        for (File file : files) {
            try {
                if (file.getTotalSpace() > biggstSpace) {
                    biggstSpace = file.getTotalSpace();
                    biggestStorage = file.getPath();
                }
            } catch (IllegalArgumentException e) {
                Log.i(TAG, "IllegalArgumentException error");
                e.printStackTrace();
            }
        }
        Log.v("", biggestStorage);
        return biggestStorage;
    }

    public static String getInternalMemoryPath(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return getBiggestStorage();
        }

    }

    public static String getFileCreateTimeStr(File f) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(
                    "cmd /C dir " + f.getAbsolutePath() + " /tc");

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            for (int i = 0; i < 6; i++) {
                line = br.readLine();
            }

            return line.substring(0, 17).replace("/", "-").replace("  ", " ")/*
                                                                              * +
                                                                              * ":00"
                                                                              */;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 返回当前路径下的所有文件夹的路径列表
     * 
     * @return 取得当前路径下的所有文件夹的路径列表
     */
    public static ArrayList<File> getCurrentDirList(String filePath) {
        ArrayList<File> file_list = new ArrayList<File>();
        File[] files = (new File(filePath)).listFiles();
        for (File _file : files)
        {
            if (_file.isDirectory() && _file.getName() != "." && _file.getName() != "..")
            {
                file_list.add(_file);
            }
        }
        return file_list;
    }

    /**
     * 获取手机内部空间大小
     * 
     * @return
     */
    public static long getStorageSize(String path) {
        if (path == null)
            return -1;
        if (!isExistFile(path)) {
            return -1;
        }
        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            return -1;
        }
        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getBlockCount(); // block总数
        return totalBlocks * blockSize;
    }

    public static long getAvailableStorageSize(String path) {
        if (path == null)
            return -1;
        if (!isExistFile(path)) {
            return -1;
        }

        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            return -1;
        }
        long blockSize = stat.getBlockSize(); // 每个block 占字节数
        long totalBlocks = stat.getAvailableBlocks(); // block总数
        return totalBlocks * blockSize;
    }

    public static List<File> getALLMemoryFile() {
        try {
            List<File> list = new ArrayList<File>();
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            String mount = new String();
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                Log.i(TAG, "mount" + line);
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat(columns[1] + "*");
                    }
                }
                else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat(columns[1] + "*");
                    }
                }
            }
            if (mount.equals("")) {
                return list;
            }
            String[] paths = mount.split("\\*");
            for (String s : paths) {
                File f = new File(s);
                list.add(f);
            }
            return list;
        } catch (Exception e) {
            Log.e("test", "e.toString()=" + e.toString());
            return null;
        }
    }

    public static long getFileSize(String path) {

        if (path == null) {
            return -1;
        }

        return getFileSize(new File(path));
    }

    public static long getFileSize(File f) {
        if (f == null) {
            return -1;
        }
        long size = 0;
        if (f.isDirectory()) {
            File[] dirFileList = f.listFiles();
            for (File item : dirFileList)
                size = size + getFileSize(item);
        } else {
            if (f.exists()) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(f);
                    size = fis.available();
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format(fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS / 1024.0) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS / 1024.0 / 1024) + "M";
        } else {
            fileSizeString = df.format(fileS / 1024.0 / 1024 / 1024) + "G";
        }
        return fileSizeString;
    }

    public static String readFile(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        try {
            input.readFully(bytes);
        } finally {
            input.close();
        }
        return new String(bytes, "UTF-8");
    }

    /**
     * 读取输入流。把输入流转化为字符串
     * 
     * @param istream
     * @return
     * @throws Exception
     */
    public static String readFileByInputStream(InputStream istream) throws IOException {
        BufferedReader br = null;
        StringBuffer buffer = new StringBuffer();
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(istream));
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
        return buffer.toString();
    }

}
