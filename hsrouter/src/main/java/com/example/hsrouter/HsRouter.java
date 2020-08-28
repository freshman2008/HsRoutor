package com.example.hsrouter;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import dalvik.system.DexFile;

public class HsRouter {
    private static volatile HsRouter instance;
    Map<String, Class<?>> routes = new HashMap<>();

    public static HsRouter getInstance() {
        if (instance == null) {
            synchronized (HsRouter.class) {
                if (instance == null) {
                    instance = new HsRouter();
                }
            }
        }
        return instance;
    }

    public void init(Application context) {
        String packageCodePath = context.getPackageCodePath();
        File dir = new File(packageCodePath).getParentFile();
//        List<String> classNames = getClassName(context, "com.hello.test");
        List<String> classNames = getClassesFromPackage(context, "com.hello.test");
        for (String classname : classNames) {
            try {
                ((IRouteRoot) (Class.forName(classname).getConstructor().newInstance())).loadInto(routes);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(21)
    public List<String> getClassesFromPackage(Context ctx, String pkgName) {
        List<String> rtnList = new ArrayList<String>();
        String[] apkPaths = ctx.getApplicationInfo().splitSourceDirs;// 获得所有的APK的路径
        DexFile dexfile = null;
        Enumeration<String> entries = null;
        String name = null;
        for (String apkPath : apkPaths) {
            try {
                dexfile = new DexFile(apkPath);// 获得编译后的dex文件
                entries = dexfile.entries();// 获得编译后的dex文件中的所有class
                while (entries.hasMoreElements()) {
                    name = (String) entries.nextElement();
                    if (name.startsWith(pkgName)) {// 判断类的包名是否符合
                        rtnList.add(name);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dexfile != null) {
                        dexfile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rtnList;
    }

    List<String> getClassName(Context context, String packageName) {
        List<String> classList = new ArrayList<>();
        ApplicationInfo applicationInfo = null;
        try {
            String sourceDir = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            DexFile dexFile = new DexFile(sourceDir);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement();
                if (name.contains(packageName)) {
                    classList.add(name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return classList;
    }

    public void navigation(Context context, String path) {
        Intent intent = new Intent(context, routes.get(path));
        context.startActivity(intent);
    }

}
