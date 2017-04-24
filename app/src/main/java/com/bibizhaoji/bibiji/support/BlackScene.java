package com.bibizhaoji.bibiji.support;

import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by EdisonChang on 2016/7/2.
 */
public class BlackScene {

    public static final String CLASSNAME_ITELEPHONY_STUB = "com.android.internal.telephony.ITelephony$Stub";
    private static Object phone;

    private static Object stubAsInterface(String clazz, IBinder binder) {
        return stubAsInterface(classForName(clazz), binder);
    }

    private static Class classForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("getClass exception, className = " + className, e);
        }
    }

    private static Object stubAsInterface(Class clazz, IBinder binder) {
        try {
            return clazz.getDeclaredMethod("asInterface", IBinder.class)
                    .invoke(null, binder);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object invoke(Object receiver, Method method, Object... paremeters) {
        Object result = null;
        try {
            result = method.invoke(receiver, paremeters);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
        }
        return result;
    }

    private static Method getMethod(String calssName, String methodName, Class<?>... paremerters) {
        Method method = null;
        try {
            Class<?> mClass = Class.forName(calssName);
            method = mClass.getDeclaredMethod(methodName, paremerters);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
        }
        return method;
    }

    public static boolean phoneIsIdle() {
        try {
            if (phone == null) {
                phone = stubAsInterface(CLASSNAME_ITELEPHONY_STUB, ServiceManager.getService("phone"));
            }
            if (phone != null) {
                return (boolean) invoke(phone, getMethod(CLASSNAME_ITELEPHONY_STUB, "isIdle"));
            }
        } catch (Exception e) {

        }
        return true;
    }

    public static class ServiceManager {

        public static IBinder getService(String serviceName) {
            try {
                return (IBinder) Class.forName("android.os.ServiceManager")
                        .getDeclaredMethod("getService", String.class)
                        .invoke(null, serviceName);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
