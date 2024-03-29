package com.cj.framework.utils

import com.cj.framework.bean.Point2D

/**
 * 贝塞尔曲线
 *
 * @author CJ
 * @date 2021/5/13 14:00
 */

/**
 * 二阶贝塞尔曲线上点获取
 * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
 *
 * @param t  曲线长度比例
 * @param p0 起始点
 * @param p1 控制点
 * @param p2 终止点
 * @return t对应的点
 */
fun calculateBezierPointForQuadratic(
    t: Float,
    p0: Point2D,
    p1: Point2D,
    p2: Point2D,
): Point2D {
    val point = Point2D(0f, 0f)
    val temp = 1 - t
    point.x = (temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x)
    point.y = (temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y)
    return point
}

/**
 * 三阶阶贝塞尔曲线上点获取
 * B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
 *
 * @param t  曲线长度比例
 * @param p0 起始点
 * @param p1 控制点1
 * @param p2 控制点2
 * @param p3 终止点
 * @return t对应的点
 */
fun calculateBezierPointForCubic(
    t: Float,
    p0: Point2D,
    p1: Point2D,
    p2: Point2D,
    p3: Point2D,
): Point2D {
    val point = Point2D(0f, 0f)
    val temp = 1 - t
    point.x =
        (p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t)
    point.y =
        (p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t)
    return point
}

