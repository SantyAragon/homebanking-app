package com.mindhub.HomeBanking.controllers;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.mindhub.HomeBanking.apis.apiCoinmarket.makeAPICall;
import static com.mindhub.HomeBanking.apis.apiCoinmarket.makeAPICall2;


@RestController
@RequestMapping("/api")
public class ApiController {
    /*
        @RequestMapping("/cryptos")
        public String getCryptos() {

    //        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
            String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
            List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
    //        paratmers.add(new BasicNameValuePair("id", "1,2,3,4,5,6,7,8,9,10"));
    //        paratmers.add(new BasicNameValuePair("amount", "1"));

            paratmers.add(new BasicNameValuePair("start", "1"));
            paratmers.add(new BasicNameValuePair("limit", "25"));

            try {
                String result = makeAPICall(uri, paratmers);
                System.out.println(result);
                return result;
            } catch (
                    IOException e) {
                System.out.println("Error: cannont access content - " + e.toString());
            } catch (
                    URISyntaxException e) {
                System.out.println("Error: Invalid URL " + e.toString());
            }
            return "null";
        }
        */
    @RequestMapping("/cryptos")
    public String getCryptos() {

//        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
        String uri = "https://api.nomics.com/v1/currencies/ticker";
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
//        paratmers.add(new BasicNameValuePair("id", "1,2,3,4,5,6,7,8,9,10"));
//        paratmers.add(new BasicNameValuePair("amount", "1"));

        paratmers.add(new BasicNameValuePair("key", "1db2bbbd970fd9ed117fff9c886a64bf2ad608b2"));
        paratmers.add(new BasicNameValuePair("ids", "BTC,ETH,XRP,USDT,BNB,ADA,DOT,AVAX,LTC,BUSD,LUNA,ETC,VET,RUNE,CAKE,TFUEL,SLP,BSW,FUN,PROM,ALPACA,LTO,TORN,NEO,PSG,BAT,ATM,OG,EZ"));
        paratmers.add(new BasicNameValuePair("interval", "1d,30d"));
        paratmers.add(new BasicNameValuePair("convert", "USD"));
        paratmers.add(new BasicNameValuePair("per-page", "100"));
        paratmers.add(new BasicNameValuePair("page", "1"));

        try {
            String result = makeAPICall2(uri, paratmers);
            System.out.println(result);
            return result;
        } catch (
                IOException e) {
            System.out.println("Error: cannont access content - " + e.toString());
        } catch (
                URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        }
        return "null";
    }

    @RequestMapping("/cryptos/img")
    public String getCryptosImg() {

        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
//        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("symbol", "BTC,ETH,BNB,USDT,ADA,DOT,BUSD,ETC,LTC,DOGE,AVAX"));
//        paratmers.add(new BasicNameValuePair("amount", "1"));

//        paratmers.add(new BasicNameValuePair("start", "1"));
//        paratmers.add(new BasicNameValuePair("limit", "25"));

        try {
            String result = makeAPICall(uri, paratmers);
            System.out.println(result);
            return result;
        } catch (
                IOException e) {
            System.out.println("Error: cannont access content - " + e.toString());
        } catch (
                URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        }
        return "null";
    }

}
