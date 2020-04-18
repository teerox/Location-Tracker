package com.example.locationmaps

import com.google.android.gms.maps.model.LatLng
import java.lang.Math.*



interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng?

    class Spherical : LatLngInterpolator {
        override fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng? {
            val fromLat: Double = toRadians(a!!.latitude)
            val fromLng: Double = toRadians(a.longitude)
            val toLat: Double = toRadians(b!!.latitude)
            val toLng: Double = toRadians(b.longitude)
            val cosFromLat: Double = cos(fromLat)
            val cosToLat: Double = cos(toLat)
            // Computes Spherical interpolation coefficients.
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle: Double = sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val num1: Double = sin((1 - fraction) * angle) / sinAngle
            val num2: Double = sin(fraction * angle) / sinAngle
            // Converts from polar to vector and interpolate.
            val x: Double = num1 * cosFromLat * cos(fromLng) + num2 * cosToLat * cos(toLng)
            val y: Double = num1 * cosFromLat * sin(fromLng) + num2 * cosToLat * sin(toLng)
            val z: Double = num1 * sin(fromLat) + num2 * sin(toLat)
            // Converts interpolated vector back to polar.
            val lat: Double = atan2(z, sqrt(x * x + y * y))
            val lng: Double = atan2(y, x)
            return LatLng(toDegrees(lat), toDegrees(lng))
        }

        private fun computeAngleBetween(
            fromLat: Double,
            fromLng: Double,
            toLat: Double,
            toLng: Double
        ): Double { // Haversine's formula
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * asin(
                sqrt(
                    pow(sin(dLat / 2), 2.0) +
                            cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2.0)
                )
            )
        }


    }
}