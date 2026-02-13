package com.mutle.mutle.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mutle.mutle.exception.CustomException;
import com.mutle.mutle.exception.ErrorCode;

public enum PlatformName {
    SPOTIFY,
    APPLE_MUSIC,
    MELON,
    YOUTUBE_MUSIC,
    SOUNDCLOUD;

    @JsonCreator
    public static PlatformName from(String value) {
        if(value==null||value.isBlank()){
            return null;
        }
        for(PlatformName platformName: PlatformName.values()){
            if(platformName.name().equals(value.toUpperCase())){
                return platformName;
            }
        }
        throw new CustomException(ErrorCode.INVALID_PLATFORM_NAME);
    }
}
