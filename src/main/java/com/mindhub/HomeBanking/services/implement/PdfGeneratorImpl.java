package com.mindhub.HomeBanking.services.implement;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.HomeBanking.models.*;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.PdfGenerator;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PdfGeneratorImpl implements PdfGenerator {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Override
    public void export(HttpServletResponse response, Authentication authentication, String numberAccount) throws IOException, DocumentException {
        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccountByNumber(numberAccount);
        Set<Transaction> transactions = account.getTransactions();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());


        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA);
        fontTitle.setSize(18);



        Paragraph paragraph = new Paragraph("Transactions of Account number: " + account.getNumber(), fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);



        //NEW TABLE
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        //HEADERS
        java.util.List<String> miLista = List.of("ID", "Type", "Description", "Amount", "Date", "Balance");


        miLista.forEach(element -> {
            PdfPCell c1 = new PdfPCell(new Phrase(element));
            c1.setBackgroundColor(new Color(93, 62, 188));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        });
        //CELLS FOR EACH TRANSACTIONS
        Comparator<Transaction> idComparator = Comparator.comparing(Transaction::getId);

        transactions.stream().sorted(idComparator).forEach(transaction -> {
            table.addCell(transaction.getId() + "");
            table.addCell(transaction.getType() + "");
            table.addCell(transaction.getDescription());
            String amount = transaction.getType() == TransactionType.DEBIT ? "-" + transaction.getAmount() : transaction.getAmount() + "";
            table.addCell(amount);


            String hours = transaction.getDate().getHour() > 9 ? transaction.getDate().getHour() + "" : "0" + transaction.getDate().getHour();
            String minutes = transaction.getDate().getMinute() > 9 ? transaction.getDate().getMinute() + "" : "0" + transaction.getDate().getMinute();

            String date = transaction.getDate().getYear() + "-" +
                    transaction.getDate().getMonthValue() + "-" +
                    transaction.getDate().getDayOfMonth() + " " +
                    hours + ":" + minutes;

            table.addCell(date);


            table.addCell(transaction.getBalance() + "");
        });


        document.add(paragraph);
        document.add(table);
        document.close();
    }


}
