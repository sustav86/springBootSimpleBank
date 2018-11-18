package com.sustavov.bank;

import com.sustavov.bank.entity.BankAccount;
import com.sustavov.bank.entity.ClientAccount;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Anton Sustavov
 * @since 2018/11/16
 */
public class BankApplicationTestImpl extends BankApplicationTests {

    private final static String EMPTY = "";
    private final static String EMISSION = "/bank/emission";
    private final static String BUYING = "/bank/buying";
    private final static String TRANSFER = "/account/transfer";

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testEmission() throws Exception {

        Map<String, String> payload = new HashMap<>();
        payload.put("amount", "50");

        MvcResult mvcResult = getMvcResult(EMISSION, super.mapToJson(payload), RequestMethod.PUT);

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        BankAccount bankAccount = super.mapFromJson(contentAsString, BankAccount.class);

        assertTrue(bankAccount.getAmount() == 50050);
    }

    @Test
    public void testBuying() throws Exception {

        Map<String, String> payload = new HashMap<>();
        payload.put("amount", "100");
        payload.put("account", "1");

        MvcResult mvcResult = getMvcResult(BUYING, super.mapToJson(payload), RequestMethod.POST);

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        ClientAccount clientAccount = super.mapFromJson(contentAsString, ClientAccount.class);

        assertTrue(clientAccount.getAmount() == 223);
    }

    @Test
    public void testTransfer() throws Exception {

        Map<String, String> payload = new HashMap<>();
        payload.put("amount", "10");
        payload.put("sender", "2");
        payload.put("recipient", "6");

        MvcResult mvcResult = getMvcResult(TRANSFER, super.mapToJson(payload), RequestMethod.POST);

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        String contentAsString = mvcResult.getResponse().getContentAsString();
        Map map = super.mapFromJson(contentAsString, Map.class);

        LinkedHashMap sender = (LinkedHashMap) map.get("sender");
        LinkedHashMap recipient = (LinkedHashMap) map.get("recipient");

        assertTrue(true);
        assertTrue((Integer) sender.get("amount") == 446);
        assertTrue((Integer) recipient.get("amount") == 887);
    }

    @Test
    public void testFindByCard() throws Exception {

        makeTransfer();

        MvcResult mvcResult = getMvcResult("/log/card/3", EMPTY, RequestMethod.GET);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 2);

        mvcResult = getMvcResult("/log/card/5", EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 1);

        mvcResult = getMvcResult("/log/card/10", EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.isEmpty());

    }

    @Test
    public void testFindByCardDate() throws Exception {

        LocalDate now = LocalDate.now();
        makeTransfer();

        MvcResult mvcResult = getMvcResult("/log/datecard/3?from=" + now + "&to=" + now.plusDays(1), EMPTY, RequestMethod.GET);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 2);

        mvcResult = getMvcResult("/log/datecard/5?from=" + now + "&to=" + now.plusDays(1), EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 1);

        mvcResult = getMvcResult("/log/datecard/10?from=" + now + "&to=" + now.plusDays(1), EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.isEmpty());
    }

    @Test
    public void testFindByClient() throws Exception {

        makeTransfer();

        MvcResult mvcResult = getMvcResult("/log/client/Frigg", EMPTY, RequestMethod.GET);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 2);

        mvcResult = getMvcResult("/log/client/Hel", EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.isEmpty());
    }

    @Test
    public void testFindByClientDate() throws Exception {

        LocalDate now = LocalDate.now();
        makeTransfer();

        MvcResult mvcResult = getMvcResult("/log/dateclient/Frigg?from=" + now + "&to=" + now.plusDays(1), EMPTY, RequestMethod.GET);
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.size() == 2);

        mvcResult = getMvcResult("/log/dateclient/Hel?from=" + now + "&to=" + now.plusDays(1), EMPTY, RequestMethod.GET);
        contentAsString = mvcResult.getResponse().getContentAsString();
        list = super.mapFromJson(contentAsString, List.class);

        assertTrue(list.isEmpty());
    }

    private MvcResult getMvcResult(String uri, String inputJson, RequestMethod requestMethod) throws Exception {

        MockHttpServletRequestBuilder content;
        if (requestMethod == RequestMethod.POST) {
            content = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson);
        } else if (requestMethod == RequestMethod.GET) {
            content = MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE);
        } else {
            content = MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson);
        }

        return mvc.perform(content).andReturn();
    }

    private void makeTransfer() throws Exception {

        Map<String, String> payload = new HashMap<>();
        payload.put("amount", "10");
        payload.put("sender", "2");
        payload.put("recipient", "6");

        MvcResult mvcResult = getMvcResult(TRANSFER, super.mapToJson(payload), RequestMethod.POST);
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        payload = new HashMap<>();
        payload.put("amount", "10");
        payload.put("sender", "2");
        payload.put("recipient", "3");

        mvcResult = getMvcResult(TRANSFER, super.mapToJson(payload), RequestMethod.POST);
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        payload = new HashMap<>();
        payload.put("amount", "10");
        payload.put("sender", "3");
        payload.put("recipient", "5");

        mvcResult = getMvcResult(TRANSFER, super.mapToJson(payload), RequestMethod.POST);
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        payload = new HashMap<>();
        payload.put("amount", "10");
        payload.put("sender", "4");
        payload.put("recipient", "1");

        mvcResult = getMvcResult(TRANSFER, super.mapToJson(payload), RequestMethod.POST);
        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

    }
}
