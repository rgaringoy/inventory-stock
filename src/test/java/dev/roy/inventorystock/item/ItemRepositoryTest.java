package dev.roy.inventorystock.item;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findByCode_shouldReturnCode_whenCodeExists() {
        //given
        String code = "CODE123456";
        Item item = new Item(
                1L,
                code,
                "Jac Daniels",
                "Nose Light with plenty of sweetnesrererr",
                5,
                null);
        underTest.save(item);
        //when
        Optional<Item> exists = underTest.findByCode(code);
        //then
        assertThat(exists).isPresent();

    }

    @Test
    void findByCode_shouldReturnCode_whenCodeDoesNotExists() {
        //given
        String code = "CODE123456";
        //when
        Optional<Item> exists = underTest.findByCode(code);
        //then
        assertThat(exists).isNotPresent();

    }
}