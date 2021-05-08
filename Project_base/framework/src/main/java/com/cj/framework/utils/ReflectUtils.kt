package com.cj.framework.utils

import java.lang.reflect.*

/**
 * 反射工具类
 *
 * @author CJ
 * @date 2021/5/7 11:36
 */

/**
 * 创建构造函数
 *
 * @param clazz javaClass
 * @param args 参数
 * @return T 对象
 */
fun <T> newInstance(clazz: Class<T>, vararg args: Any?): T {
    return clazz.getConstructor(*getParameterTypes(*args)).newInstance(*args) as T
}

/**
 * 获取成员变量
 *
 * @param className 类名
 * @param fieldName 变量名称
 * @param args 构造函数参数
 * @return 成员变量
 */
fun <T> getField(className: String, fieldName: String, vararg args: Any?): T? {
    return getField(Class.forName(className), fieldName, *args)
}

/**
 * 获取成员变量
 *
 * @param clazz JavaClass
 * @param fieldName 变量名称
 * @param args 构造函数参数
 * @return 成员变量
 */
fun <T> getField(clazz: Class<*>, fieldName: String, vararg args: Any?): T? {
    return getField(newInstance(clazz, *args), fieldName)
}

/**
 * 获取成员变量
 *
 * @param any 实例对象
 * @param fieldName 变量名称
 * @return 成员变量
 */
@Suppress("UNCHECKED_CAST")
fun <T> getField(any: Any, fieldName: String): T? {
    val field = getAccessibleField(any.javaClass, fieldName)
    return field.get(any)?.let {
        it as T
    }
}

/**
 * 更新成员变量值
 *
 * @param any 实例对象
 * @param fieldName 变量名称
 * @param value 值
 */
fun updateField(any: Any, fieldName: String, value: Any?) {
    getAccessibleField(any.javaClass, fieldName).set(any, value)
}

/**
 * 执行反射方法
 *
 * @param any 实例对象
 * @param methodName 方法名称
 * @param args 方法所需参数
 * @return 方法返回值
 */
fun invokeMethod(any: Any, methodName: String, vararg args: Any?): Any? {
    return getAccessibleMethod(any.javaClass, methodName, *getParameterTypes(args))
        .invoke(any, *args)
}

/**
 * 获取参数类型
 */
private fun getParameterTypes(vararg any: Any?): Array<Class<*>?> {
    val types = arrayOfNulls<Class<*>>(any.size)
    for (i in any.indices) {
        types[i] = any[i]?.let {
            when (it) {
                is Byte -> {
                    Byte::class.java
                }
                is Short -> {
                    Short::class.java
                }
                is Int -> {
                    Int::class.java
                }
                is Long -> {
                    Long::class.java
                }
                is Float -> {
                    Float::class.java
                }
                is Double -> {
                    Double::class.java
                }
                is Boolean -> {
                    Boolean::class.java
                }
                is Char -> {
                    Char::class.java
                }
                else -> {
                    it.javaClass
                }
            }
        }
    }
    return types
}

private fun getAccessibleField(clazz: Class<*>, fieldName: String): Field {
    var type: Class<*>? = clazz
    var field: Field?

    try {
        field = accessible(type?.getField(fieldName))
    } catch (e: Exception) {
        do {
            try {
                field = accessible(type?.getDeclaredField(fieldName))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            type = type?.superclass
        } while (type != null)
        throw NoSuchFieldException("No such field")
    }

    field.run {
        if (modifiers and Modifier.FINAL == Modifier.FINAL) {
            try {
                val modifiersField = Field::class.java.getDeclaredField("modifiers")
                modifiersField.isAccessible = true
                modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }
    }
    return field
}

private fun getAccessibleMethod(
    clazz: Class<*>,
    methodName: String,
    vararg parameterTypes: Class<*>?
): Method {
    var type: Class<*>? = clazz
    try {
        return accessible(type?.getMethod(methodName, *parameterTypes))
    } catch (e: Exception) {
        do {
            try {
                return accessible(type?.getDeclaredMethod(methodName, *parameterTypes))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            type = type?.superclass
        } while (type != null)
        throw NoSuchFieldException("No such field")
    }
}

private fun <T : AccessibleObject> accessible(accessible: T?): T {
    return accessible?.let {
        if (it is Member) {
            val member = it as Member
            if (Modifier.isPublic(member.modifiers)
                && Modifier.isPublic(member.declaringClass.modifiers)
            ) {
                return it
            }
        }
        if (!it.isAccessible) {
            it.isAccessible = true
        }
        it
    } ?: throw IllegalStateException("accessible is null")
}


