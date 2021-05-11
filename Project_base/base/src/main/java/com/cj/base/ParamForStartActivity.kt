package com.cj.base

import androidx.annotation.StringDef

/**
 * activity启动参数
 *
 * @author CJ
 * @date 2021/5/8 15:16
 */
const val CLASS = "class"//javaClass
const val FRAGMENT_CANONICAL_NAME = "canonical_name"//fragment canonical name
const val BUNDLE = "bundle"//Bundle数据

@StringDef(CLASS, FRAGMENT_CANONICAL_NAME, BUNDLE)
@Retention(AnnotationRetention.SOURCE)
annotation class ParamForStartActivity
