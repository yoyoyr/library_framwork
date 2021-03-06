package com.tde.framework.utils;

import androidx.annotation.NonNull;

import com.tde.framework.base.BaseViewModel;
import com.tde.framework.base.model.BaseModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 反射工具类
 */
public class ReflectionUtil {

    /**
     * Desc:
     * 普通的非泛型类Class
     * 泛型化的类Class<T>
     * JDK中，普通的Class.newInstance()方法的定义返回Object，要将该返回类型强制转换为另一种类型;
     * 但是使用泛型的Class<T>，Class.newInstance()方法具有一个特定的返回类型;
     * <p>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T getNewInstance(Object object, int i) {
        if (object != null) {
            try {
                Type type;
                if (object instanceof Class) {
                    type = ((Class) object).getGenericSuperclass();
                } else {
                    type = object.getClass().getGenericSuperclass();
                }
                if (type instanceof ParameterizedType) {
                    return ((Class<T>) ((ParameterizedType) (type)).getActualTypeArguments()[i])
                            .newInstance();
                } else {
                    Class<?> superclass;
                    if (object instanceof Class) {
                        superclass = ((Class) object).getSuperclass();
                    } else {
                        superclass = object.getClass().getSuperclass();
                    }
                    if (superclass == BaseViewModel.class || superclass == Object.class) {
                        return (T) new BaseModel();
                    } else {
                        return getNewInstance(superclass, i);
                    }
                }

            } catch (InstantiationException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }

        }
        return (T) new BaseModel();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            return (T) ((ParameterizedType) object.getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[i];
        }
        return null;
    }

    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }


}
