package io.tryrook.rooknative.core.framework.extension

import com.rookmotion.rook.sdk.domain.model.AuthorizedDataSources

fun AuthorizedDataSources.isAtLeastOneConnected(): Boolean {
    val oura2 = oura == true
    val polar2 = polar == true
    val whoop2 = whoop == true
    val fitbit2 = fitbit == true
    val garmin2 = garmin == true
    val withings2 = withings == true
    val dexcom2 = dexcom == true

    return oura2 || polar2 || whoop2 || fitbit2 || garmin2 || withings2 || dexcom2
}
