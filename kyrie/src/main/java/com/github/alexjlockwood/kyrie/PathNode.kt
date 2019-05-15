package com.github.alexjlockwood.kyrie

import android.graphics.Path

/** A [Node] that paints a path. */
class PathNode private constructor(
        rotation: List<Animation<*, Float>>,
        pivotX: List<Animation<*, Float>>,
        pivotY: List<Animation<*, Float>>,
        scaleX: List<Animation<*, Float>>,
        scaleY: List<Animation<*, Float>>,
        translateX: List<Animation<*, Float>>,
        translateY: List<Animation<*, Float>>,
        fillColor: List<Animation<*, Int>>,
        fillAlpha: List<Animation<*, Float>>,
        strokeColor: List<Animation<*, Int>>,
        strokeAlpha: List<Animation<*, Float>>,
        strokeWidth: List<Animation<*, Float>>,
        trimPathStart: List<Animation<*, Float>>,
        trimPathEnd: List<Animation<*, Float>>,
        trimPathOffset: List<Animation<*, Float>>,
        @StrokeLineCap strokeLineCap: Int,
        @StrokeLineJoin strokeLineJoin: Int,
        strokeMiterLimit: List<Animation<*, Float>>,
        strokeDashArray: List<Animation<*, FloatArray>>,
        strokeDashOffset: List<Animation<*, Float>>,
        @FillType fillType: Int,
        isStrokeScaling: Boolean,
        // TODO: finalize API
        fillColorComplex: ComplexColor?,
        strokeColorComplex: ComplexColor?,
        private val pathData: List<Animation<*, PathData>>
) : RenderNode(
        rotation,
        pivotX,
        pivotY,
        scaleX,
        scaleY,
        translateX,
        translateY,
        fillColor,
        fillAlpha,
        strokeColor,
        strokeAlpha,
        strokeWidth,
        trimPathStart,
        trimPathEnd,
        trimPathOffset,
        strokeLineCap,
        strokeLineJoin,
        strokeMiterLimit,
        strokeDashArray,
        strokeDashOffset,
        fillType,
        isStrokeScaling,
        fillColorComplex,
        strokeColorComplex
) {

    // <editor-fold desc="Layer">

    override fun toLayer(timeline: PropertyTimeline): PathLayer {
        return PathLayer(timeline, this)
    }

    internal class PathLayer(timeline: PropertyTimeline, node: PathNode) : RenderNode.RenderLayer(timeline, node) {
        private val pathData = registerAnimatableProperty(node.pathData)

        override fun onInitPath(outPath: Path) {
            PathData.toPath(pathData.animatedValue, outPath)
        }
    }

    // </editor-fold>

    // <editor-fold desc="Builder">

    @DslMarker
    private annotation class PathNodeMarker

    /** Builder class used to create [PathNode]s. */
    @PathNodeMarker
    class Builder internal constructor() : RenderNode.Builder<Builder>() {
        private val pathData = asAnimations(PathData())

        // Path data.

        fun pathData(initialPathData: String): Builder {
            return pathData(PathData.parse(initialPathData))
        }

        fun pathData(initialPathData: PathData): Builder {
            return replaceFirstAnimation(pathData, asAnimation(initialPathData))
        }

        @SafeVarargs
        fun pathData(vararg animations: Animation<*, PathData>): Builder {
            return replaceAnimations(pathData, *animations)
        }

        fun pathData(animations: List<Animation<*, PathData>>): Builder {
            return replaceAnimations(pathData, animations)
        }

        override fun self(): Builder {
            return this
        }

        override fun build(): PathNode {
            return PathNode(
                    rotation,
                    pivotX,
                    pivotY,
                    scaleX,
                    scaleY,
                    translateX,
                    translateY,
                    fillColor,
                    fillAlpha,
                    strokeColor,
                    strokeAlpha,
                    strokeWidth,
                    trimPathStart,
                    trimPathEnd,
                    trimPathOffset,
                    strokeLineCap,
                    strokeLineJoin,
                    strokeMiterLimit,
                    strokeDashArray,
                    strokeDashOffset,
                    fillType,
                    isScalingStroke,
                    fillColorComplex,
                    strokeColorComplex,
                    pathData
            )
        }
    }

    // </editor-fold>

    companion object {

        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }
}
