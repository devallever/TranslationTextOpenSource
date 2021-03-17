package com.allever.lib.common.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    private static final String LOG_TAG = "FileUtil";

    /**
     * 获取压缩包中的单个文件InputStream
     *
     * @param zipFilePath    压缩文件的完整路径
     * @param singleFileName 压缩包中要解压的文件名 <B>（该目录下的路径)</B>
     * @return InputStream
     * @throws Exception
     */
    public static InputStream unzipSingleFile(String zipFilePath,
                                              String singleFileName) throws Exception {
        ZipFile zipFile = new ZipFile(zipFilePath);
        ZipEntry zipEntry = zipFile.getEntry(singleFileName);

        return zipFile.getInputStream(zipEntry);
    }

//    public static String getFilterCachePath(String fileName) {
//        return FILTER_CACHE_PATH + File.separator + fileName;
//    }

    /**
     * 导出ZIP文件的注释
     *
     * @param filename 指定的ZIP文件的完全路径
     * @return ZIP文件的注释，如果找不到任何注释将返回null
     */
    public static String extractZipComment(String filename) {
        if (filename == null || filename.equals("")) return null;

        String retStr = null;
        try {
            File file = new File(filename);
            int fileLen = (int) file.length();

            FileInputStream in = new FileInputStream(file);

            byte[] buffer = new byte[Math.min(fileLen, 2048)];
            int len;

            in.skip(fileLen - buffer.length);

            if ((len = in.read(buffer)) > 0) {
                retStr = getZipCommentFromBuffer(buffer, len);
                if (retStr != null) {
                    retStr = retStr.trim();
                }
            }

            in.close();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Exception on reading ZIP comment!", e);
        }
        return retStr;
    }

    private static String getZipCommentFromBuffer(byte[] buffer, int len) {
        byte[] magicDirEnd = {0x50, 0x4b, 0x05, 0x06};
        int buffLen = Math.min(buffer.length, len);

        // Check the buffer from the end
        for (int i = buffLen - magicDirEnd.length - 22; i >= 0; i--) {
            boolean isMagicStart = true;
            for (int k = 0; k < magicDirEnd.length; k++) {
                if (buffer[i + k] != magicDirEnd[k]) {
                    isMagicStart = false;
                    break;
                }
            }

            if (isMagicStart) {
                // Magic Start found!
                int commentLen = buffer[i + 20] + buffer[i + 21] * 256;
                int realLen = buffLen - i - 22;
                if (commentLen != realLen) {
                    Log.w(LOG_TAG, "ZIP comment size mismatch!");
                }
                String comment = new String(
                        buffer, i + 22, Math.min(commentLen, realLen));
                return comment;
            }
        }

        Log.w(LOG_TAG, "ZIP comment NOT found!");
        return null;
    }




    /**
     * 删除指定文件夹中的所有文件
     *
     * @param folderPath 目标文件夹的路径
     * @return 是否成功删除
     */
    public static boolean deleteFilesInFolder(String folderPath) {
        File folder = new File(folderPath);
        try {
            // 如果图片存放的路径存在并且是目录，则循环删除里面的所有文件
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    file.delete();
                }
                return true;
            } else {
            }
        } catch (Exception e) {

        }

        return false;
    }

    /**
     * 检查文件是否存在
     *
     * @param path 文件的路径
     * @return 文件是否存在
     */
    public static boolean checkExist(String path) {
        if (path == null || path.equals("")) {
            return false;
        }

        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * 检查并建立指定的文件夹
     *
     * @param folderPath 文件夹的路径
     * @return 是否建立了文件夹
     */
    public static boolean buildFolderIfNotFound(String folderPath) {
        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                if (folder.mkdirs() == false) {
                    Log.i(LOG_TAG, "The folder is already exist: " + folderPath);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            Log.w(LOG_TAG, "Fail to build folder: " + folderPath +
                    ", " + ex.getMessage());
            return false;
        }
    }


    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }

        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 复制asset文件到指定目录
     *
     * @param oldPath asset下的路径
     * @param newPath SD卡下保存路径
     */
    public static void copyAssets(AssetManager assetManager, String oldPath, String newPath) {
        try {
            InputStream is = assetManager.open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            is.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @return 是否成功移动文件
     */
    public static boolean moveFile(File sourceFile, File targetFile) {
        if (sourceFile == null) {
            Log.w(LOG_TAG, "Argument 'sourceFile' is null.");
            return false;
        }
        if (targetFile == null) {
            Log.w(LOG_TAG, "Argument 'targetFile' is null.");
            return false;
        }

        try {
            copyFile(sourceFile, targetFile);
            deleteFile(sourceFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Exception on moveFile(): " + e.getMessage());
            return false;
        }
    }


    /**
     * 删除指定的路径的文件
     *
     * @param filePath 指定文件的完全路径
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Exception on deleting file: " + filePath +
                    ", " + e.getMessage());
        }
    }

    /**
     * 删除一个文件夹
     *
     * @param folderPath
     */
    public static void deleteFolder(String folderPath) {
        try {
            File file = new File(folderPath);
            if (!file.exists()) {
                return;
            }
            deleteAllFiles(folderPath);
            file.delete();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private static void deleteAllFiles(String folderPath) {
        try {
            File file = new File(folderPath);
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File item : listFiles) {
                    if (item.isFile()) {
                        item.delete();
                    } else {
                        deleteFolder(item.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 判断SD卡是否可用
     *
     * @return SD卡是否可用
     */
    public static boolean isSDCardMounted() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 调用媒体扫描服务扫描整个外部存储器</br>
     * 建议采用{@link #callToScanMediaFile(Context, String)}来扫描指定文件以提高效率
     *
     * @param context 上下文对象
     */
    public static void callToScanMediaFile(Context context) {
        if (context == null) {
            Log.w(LOG_TAG, "Argument 'context' is null.");
            return;
        }

        context.sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    /**
     * 调用媒体扫描服务扫描指定的图像文件
     *
     * @param context  上下文对象
     * @param filePath 指定的图像文件的完全路径，不需要包含"<code>file://</code>"的前缀
     */
    public static void callToScanMediaFile(Context context, String filePath) {
        if (context == null) {
            Log.w(LOG_TAG, "Argument 'context' is null.");
            return;
        }
        if (filePath == null || filePath.length() == 0) {
            Log.w(LOG_TAG, "Argument 'filePath' is null or empty.");
            return;
        }

        Uri uri = Uri.parse("file://" + filePath);
        if (uri == null) {
            Log.w(LOG_TAG, "Error on parsing file path to URI, filePath: " + filePath);
            return;
        }

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }


    private static final String SCHEME_FILE = "file";
    private static final String[] IMAGE_FILE_EXT = {"jpg", "jpeg", "gif", "png", "bmp"};

    /**
     * 判断某URI地址是不是非图像文件的地址
     *
     * @param uri 要判断的URI地址
     * @return 如果该地址是非文件地址，或其指向的是图像文件，则返回false；如果是其他非图像文件则返回true
     */
    public static boolean isNonImageFileUri(Uri uri) {
        if (uri == null) {
            Log.w(LOG_TAG, "Argument 'uri' is null.");
            return false;
        }
        if (!uri.isHierarchical()) {
            return false;
        }

        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equals(SCHEME_FILE)) {
            return false;
        }

        String path = uri.getPath();
        int extStartIndex = path.lastIndexOf('.');
        if (extStartIndex <= 0 || extStartIndex >= path.length() - 1) {
            return true;
        }

        String ext = uri.getPath().substring(extStartIndex + 1);
        for (int i = 0; i < IMAGE_FILE_EXT.length; i++) {
            if (ext.equalsIgnoreCase(IMAGE_FILE_EXT[i])) {
                return false;
            }
        }

        return true;
    }


    /**
     * 保存数据到指定文件
     *
     * @param byteData
     * @param filePathName
     * @return true for save successful, false for save failed.
     */
    public static boolean saveByteToSDFile(final byte[] byteData, final String filePathName) {
        boolean result = false;
        try {
            File newFile = createNewFile(filePathName, false);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            fileOutputStream.write(byteData);
            fileOutputStream.flush();
            fileOutputStream.close();
            result = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }

    public static boolean saveStringToFile(final String str, final String filePathName) {
        boolean result = false;
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter writer = null;
        try {
            File newFile = createNewFile(filePathName, false);
            fileOutputStream = new FileOutputStream(newFile);
            writer = new OutputStreamWriter(fileOutputStream);
            writer.append(str);
            writer.flush();
            result = true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e1) {

            }
        }
        return result;
    }

    public static void createDir(String dir) {
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    /**
     * @param path：文件路径
     * @param append：若存在是否插入原文件
     * @return
     */
    public static File createNewFile(String path, boolean append) {
        File newFile = new File(path);
        if (!append) {
            if (newFile.exists()) {
                newFile.delete();
            }
        }
        if (!newFile.exists()) {
            try {
                File parent = newFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                newFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    /**
     * 获取媒体文件路径
     *
     * @param context
     * @param uri
     * @param c
     * @return
     */
    public static String getMediaFilePath(Context context, Uri uri, Cursor c) {
        String filePath = null;
        try {
            filePath = c.getString(c.getColumnIndexOrThrow(Images.Media.DATA));
        } catch (IllegalArgumentException e) {
            try {
                filePath = c.getString(c.getColumnIndexOrThrow("_data"));
            } catch (IllegalArgumentException ex) {
                filePath = uri.getPath();
            }
        }
        return filePath;
    }

    /**
     * 获取媒体文件类型
     *
     * @param context
     * @param uri
     * @param c
     * @return
     */
    public static String getMediaFileMimeType(Context context, Uri uri, Cursor c) {
        String contentType = null;
        try {
            contentType = c.getString(
                    c.getColumnIndexOrThrow(Images.Media.MIME_TYPE)); // mime_type
        } catch (IllegalArgumentException e) {
            try {
                contentType = c.getString(c.getColumnIndexOrThrow("mimetype"));
            } catch (IllegalArgumentException ex) {
                contentType = context.getContentResolver().getType(uri);
            }
        }
        return contentType;
    }

    public static void saveMediaFile(Context context, Uri uri, String filePath) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(filePath);
            byte[] buffer = new byte[8192];
            for (int len = 0; (len = is.read(buffer)) != -1; ) {
                os.write(buffer, 0, len);
            }
        } catch (Throwable e) {
            Log.e("", "", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Throwable e) {
                }
            }
        }
    }

    public static void takePersistableUriPermission(Context context, Uri uri, int takeFlags) {
        try {
            Method takePersistableUriPermissionMethod = ContentResolver.class.getMethod(
                    "takePersistableUriPermission", new Class<?>[]{
                            Uri.class, int.class
                    });
            takePersistableUriPermissionMethod.invoke(context.getContentResolver(), uri, takeFlags);
        } catch (Throwable tr) {
            Log.e("", "", tr);
        }
    }

    /**
     * 检查文件路径是否缺少SD卡路径，如果缺少返回完整的路径
     *
     * @param path
     * @return
     */
    public static String checkLackESD(String path) {
        if (TextUtils.isEmpty(path)) {
            return path;
        }

        try {
            File file = new File(path);
            if (file != null && !file.canRead()) {
                try {
                    Log.e("FileUtil", "Path Error:" + path);
                } catch (Throwable t) {
                }
                if (!path.startsWith("/mnt/sdcard")
                        && !path.startsWith(Environment.getExternalStorageDirectory().getPath())) {
                    try {
                    } catch (Throwable t) {
                    }
                    String fixedPath = Environment.getExternalStorageDirectory().getPath() + path;
                    File fixedFile = new File(fixedPath);
                    if (fixedFile.canRead() && fixedFile.isFile()) {
                        try {
                        } catch (Throwable t) {
                        }
                        return fixedPath;
                    }
                }
            }
        } catch (Throwable tr) {
            Log.e("FileUtil", "", tr);
        }

        return path;
    }

    /**
     * 通过文件路径获取文件大小
     */
    public static long getFileSize(String path) {
        try {
            Log.e("FileUtil", "FileSize Error:" + path);
        } catch (Throwable t) {
        }

        if (path == null) {
            return 0;
        }

        try {
            File file = new File(path);
            return file.length();
        } catch (Throwable tr) {
            Log.e("FileUtil", "", tr);
        }

        return 0;
    }

    /**
     * 保存数据到文件
     *
     * @param filePath 要保存到的文件路径
     * @param files    要保存的二进制数据
     */
    public static void savePicToFile(String filePath, byte[] files) {
        FileOutputStream out = null;
        try {
            File newFile = new File(filePath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            out = new FileOutputStream(newFile);
            out.write(files);

        } catch (IOException e) {

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 彩信附件文件名处理
     *
     * @param src
     * @return
     */
    public static String getMmsPartSrc(String src) {
        if (TextUtils.isEmpty(src)) {
            return src;
        }

        src = src.replace(' ', '_');
        src = src.replace('=', '_');

        if (src.length() > 20) {
            src = src.substring(src.length() - 20);
        }

        return src;
    }

    public static final String GOSHARE_MEDIA_FILE_SAVE_DIR = Environment.getExternalStorageDirectory() + "/ZEROSMS/.goshare/";


    public static boolean isExistsFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 判断文件内容是否为空
     *
     * @param file
     * @return
     */
    public static boolean isFileEmpty(String file) {
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            if (fr.read() == -1) {
                fr.close();
                return true;
            } else {
                fr.close();
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }


    // String.format("%.2f", Float.valueOf(size)/1024/1024);
    public static String formetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.#");// #代表数字
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((float) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((float) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((float) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((float) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取文件的名称
     *
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {
        if (TextUtils.isEmpty(pathName)) return null;
        String name = pathName.substring(pathName.lastIndexOf(File.separator) + 1, pathName.length());
        return name;
    }

    /**
     * 获取父文件的路径
     *
     * @param pathName
     * @return
     */
    public static String getParentFilePath(String pathName) {
        if (TextUtils.isEmpty(pathName)) return null;
        return (pathName.substring(0, pathName.lastIndexOf(File.separator)));
    }

    public static String readInputStream(InputStream in, String charset) throws IOException {
        if (in == null) {
            return "";
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean var3 = true;

            try {
                byte[] buf = new byte[1024];
                boolean var6 = false;

                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                byte[] data = out.toByteArray();
                String var7 = new String(data, TextUtils.isEmpty(charset) ? "UTF-8" : charset);
                return var7;
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }

            }

            return null;
        }
    }

    public static String readFileToString(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            } else {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    return readInputStream(inputStream, "UTF-8");
                } catch (Exception var3) {
                    var3.printStackTrace();
                    return null;
                }
            }
        }
    }




    public static boolean inputStream2File(InputStream ins, File outFile) {
        try {
            OutputStream outStream = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = ins.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            ins.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        if (uri == null) {
            return "";
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                //
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }else {
                //Log.e("路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
