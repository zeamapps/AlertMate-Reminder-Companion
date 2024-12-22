package com.zeamapps.snoozy.domain

import kotlinx.coroutines.flow.Flow

class ReadAppEntry(var localUserManager: LocalUserManager) {
    operator fun invoke(): Flow<Boolean> {
        return localUserManager.readAppEntry()
    }
}