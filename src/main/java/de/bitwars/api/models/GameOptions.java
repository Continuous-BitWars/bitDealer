/*
 * Copyright (C) 2024 Andreas Heinrich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.bitwars.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Objects;


@JsonTypeName("GameOptions")
public class GameOptions {
    private Integer stepSleepDuration;


    @JsonProperty("step_sleep_duration")
    public Integer getStepSleepDuration() {
        return stepSleepDuration;
    }

    @JsonProperty("step_sleep_duration")
    public void setStepSleepDuration(Integer stepSleepDuration) {
        this.stepSleepDuration = stepSleepDuration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameOptions gameOptions = (GameOptions) o;
        return Objects.equals(this.stepSleepDuration, gameOptions.stepSleepDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepSleepDuration);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GameOptions {\n");

        sb.append("    stepSleepDuration: ").append(toIndentedString(stepSleepDuration)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


}

