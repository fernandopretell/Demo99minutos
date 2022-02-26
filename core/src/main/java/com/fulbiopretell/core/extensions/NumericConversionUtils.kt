package com.fulbiopretell.core.extensions

import java.text.NumberFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

object NumericConversionUtils {

    fun getFormattedNumber(count: Long): String {
        val parameter: Int
        val turningNumber: Int
        val unit: String
        parameter = 1000
        turningNumber = 10000
        unit = "K"

        if (count < turningNumber) return NumberFormat.getNumberInstance(Locale.US).format(count)

        val exp = (ln(count.toDouble()) / ln(parameter.toDouble())).toInt()
        val premise = NumberFormat.getNumberInstance(Locale.US)
            .format(count / parameter.toDouble().pow(exp.toDouble()))

        val firstNumber = premise.substringBefore(".")
        if (premise.contains(".")) {
            val decimal = premise.substringAfter(".").take(1)
            if (decimal != "0") {
                return "$firstNumber.$decimal$unit"
            }
        }

        return "$firstNumber$unit"
    }

}