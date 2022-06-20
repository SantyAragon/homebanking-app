package com.mindhub.HomeBanking;

import com.mindhub.HomeBanking.Utils.utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class UtilsTest {

    @Test
    public void existCardNumberTest() {
        String cardnumber = String.valueOf(utils.randomNumber(1000, 9999) + utils.randomNumber(1000, 9999) + utils.randomNumber(1000, 9999) + utils.randomNumber(1000, 9999));
        assertThat(cardnumber, is(not(emptyOrNullString())));
    }

    @Test
    public void lengthCardNumberTest() {
        String cardnumber = utils.randomNumber(1000, 9999) +""+ utils.randomNumber(1000, 9999) +""+  utils.randomNumber(1000, 9999) +""+  utils.randomNumber(1000, 9999);
        assertThat(cardnumber, hasLength(16));
    }

    @Test
    public void cvvNumberTest() {
        int cvv = utils.randomNumber(100, 999);
        assertThat(cvv, is(not(cvv < 0 || cvv > 999)));
    }

    @Test
    public void cvvNumberLengthTest(){
        int cvv = utils.randomNumber(100, 999);
        assertThat(String.valueOf(cvv), hasLength(3));
    }

    @Test
    public void numberAccount(){
        String number =  String.valueOf(utils.randomNumber(10000000,99999999));
        assertThat(String.valueOf(number), hasLength(8));
    }

}
