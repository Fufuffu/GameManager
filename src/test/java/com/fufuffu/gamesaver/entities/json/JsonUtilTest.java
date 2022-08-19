package com.fufuffu.gamesaver.entities.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilTest {

    public static class TestPerson {
        private String name;
        private String surname;

        public TestPerson() {

        }

        public TestPerson(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestPerson) {
                return ((TestPerson) obj).name.equals(this.name) && ((TestPerson) obj).surname.equals(this.surname);
            }
            return super.equals(obj);
        }
    }

    @Test
    public void shouldConvertToJsonString() {
        TestPerson person = new TestPerson("Pepe", "Prueba");

        String expectedJson = "{\"name\":\"Pepe\",\"surname\":\"Prueba\"}";
        assertEquals(Optional.of(expectedJson), JsonUtil.toJson(person));
    }

    @Test
    public void shouldConvertToObjectFromJson() {
        String inputJson = "{\"name\":\"Pepe\",\"surname\":\"Prueba\"}";

        TestPerson expectedPerson = new TestPerson("Pepe", "Prueba");

        Optional<TestPerson> actual = JsonUtil.fromJson(inputJson, TestPerson.class);
        assertEquals(Optional.of(expectedPerson), actual);
    }

    @Test
    public void shouldConvertToObjectFromJsonWithUnknownFields() {
        String inputJson = "{\"name\":\"Pepe\",\"surname\":\"Prueba\",\"unk\":\"test\"}";
        JsonUtil.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TestPerson expectedPerson = new TestPerson("Pepe", "Prueba");

        Optional<TestPerson> actual = JsonUtil.fromJson(inputJson, TestPerson.class);
        assertEquals(Optional.of(expectedPerson), actual);
    }
}
