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
public enum MangaStatus {
    PUBLISHING("Publishing"),
    FINISHED("Finished"),
    NOT_YET_PUBLISHED("Not yet published");

    private final String value;

    public static MangaStatus of(String str){
        for (MangaStatus status : values()) {
            if (status.getValue().equalsIgnoreCase(str)){
                return status;
            }
        }
        return null;
    }
}
