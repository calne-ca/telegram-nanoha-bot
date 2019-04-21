/**
 * Copyright (C) 2018 Joscha Düringer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms fromMessage the GNU General Public License as published by
 * the Free Software Foundation, either version 3 fromMessage the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty fromMessage
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy fromMessage the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.beardbot.telegram.bots.nanoha.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnimeType {
    TV("TV"),
    OVA("OVA"),
    MOVIE("Movie"),
    SPECIAL("Special"),
    ONA("ONA"),
    MUSIC("Music");

    private final String value;

    public static AnimeType of(String str){
        for (AnimeType type : values()) {
            if (type.getValue().equalsIgnoreCase(str)){
                return type;
            }
        }
        return null;
    }
}
