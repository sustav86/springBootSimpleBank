package com.sustavov.bank;

import com.sustavov.bank.entity.BankAccount;
import com.sustavov.bank.entity.ClientAccount;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Anton Sustavov
 * @since 2018/11/16
 */
public class BankApplicationTestImpl extends BankApplicationTests {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testEmission() throws Exception {

        String uri = "/bank/emission";
        Map<String, String> payload = new HashMap<>();
        payload.put("amount","50");

        String inputJson = super.mapToJson(payload);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        BankAccount bankAccount = super.mapFromJson(contentAsString, BankAccount.class);

        assertTrue(bankAccount.getAmount() == 49950);
    }

    @Test
    public void testBuying() throws Exception {

        String uri = "/bank/buying";
        Map<String, String> payload = new HashMap<>();
        payload.put("amount","100");
        payload.put("account","1");

        String inputJson = super.mapToJson(payload);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ClientAccount clientAccount = super.mapFromJson(contentAsString, ClientAccount.class);

        assertTrue(clientAccount.getAmount() == 223);
    }

    @Test
    public void testTransfer() throws Exception {

        String uri = "/account/transfer";
        Map<String, String> payload = new HashMap<>();
        payload.put("amount","10");
        payload.put("sender","2");
        payload.put("recipient","6");

        String inputJson = super.mapToJson(payload);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = super.mapFromJson(contentAsString, Map.class);

        LinkedHashMap sender = (LinkedHashMap) map.get("sender");
        LinkedHashMap recipient = (LinkedHashMap) map.get("recipient");

        assertTrue(true);
        assertTrue((Integer)sender.get("amount") == 446);
        assertTrue((Integer)recipient.get("amount") == 887);
    }

}
