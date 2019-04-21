package net.beardbot.telegram.bots.nanoha.handler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestType {
    ANIME("a: "),
    MANGA("m: "),
    BOTH("am: "),
    NONE("");

    private final String prefix;

    public static RequestType fromMessage(String message){
        if (message.toLowerCase().startsWith(ANIME.prefix)){
            return ANIME;
        } else if (message.toLowerCase().startsWith(MANGA.prefix)){
            return MANGA;
        } else if (message.toLowerCase().startsWith(BOTH.prefix)){
            return BOTH;
        }

        return NONE;
    }

    public String extractQuery(String message){
        return message.substring(prefix.length());
    }
}
