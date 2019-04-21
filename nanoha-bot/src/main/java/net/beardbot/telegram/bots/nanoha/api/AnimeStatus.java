package net.beardbot.telegram.bots.nanoha.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnimeStatus {
    NOT_YET_AIRED("Not yet aired"),
    CURRENTLY_AIRING("Currently Airing"),
    FINISHED_AIRING("Finished Airing");

    private final String value;

    public static AnimeStatus of(String str){
        for (AnimeStatus status : values()) {
            if (status.getValue().equalsIgnoreCase(str)){
                return status;
            }
        }
        return null;
    }
}
