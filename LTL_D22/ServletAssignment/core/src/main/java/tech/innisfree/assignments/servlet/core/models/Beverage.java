/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tech.innisfree.assignments.servlet.core.models;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonAutoDetect
public class Beverage {

    @JsonProperty
    private String name;

    @JsonProperty
    private String manufactureName;

    public Beverage(String name, String manufactureName) {
        this.name = name;
        this.manufactureName = manufactureName;
    }

    public Beverage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getManufactureName() {
        return manufactureName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufactureName(String manufactureName) {
        this.manufactureName = manufactureName;
    }

    @Override
    public String toString() {
        return "Beverage{" +
                "name='" + name + '\'' +
                ", manufactureName='" + manufactureName + '\'' +
                '}';
    }
}