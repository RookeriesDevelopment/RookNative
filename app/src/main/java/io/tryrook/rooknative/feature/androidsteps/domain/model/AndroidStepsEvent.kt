package io.tryrook.rooknative.feature.androidsteps.domain.model

interface AndroidStepsEvent {
    data object AndroidStepsEnabled : AndroidStepsEvent
    data object MissingPermissions : AndroidStepsEvent
}