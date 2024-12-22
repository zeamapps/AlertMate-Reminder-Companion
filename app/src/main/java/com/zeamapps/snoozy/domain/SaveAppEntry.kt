package com.zeamapps.snoozy.domain

class SaveAppEntry(var localUserManager: LocalUserManager)  {
    suspend operator fun invoke() {
        localUserManager.saveAppEntry()
    }

}