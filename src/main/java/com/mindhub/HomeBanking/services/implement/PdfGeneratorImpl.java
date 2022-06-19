package com.mindhub.HomeBanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class PdfGeneratorImpl implements PdfGenerator {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;

    @Override
    public void export(HttpServletResponse response, Authentication authentication, String numberAccount, LocalDateTime since, LocalDateTime until) throws IOException, DocumentException {
        Account account = accountService.getAccountByNumber(numberAccount);
        Set<Transaction> transactions = account.getTransactions();

        Set<Transaction> intervalTransactions = transactions.stream()
                .filter(transaction -> transaction.getDate().isAfter(since) && transaction.getDate().isBefore(until))
                .collect(Collectors.toSet());

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA);
        fontTitle.setSize(18);

        Image logo = Image.getInstance("./src/main/resources/static/web/assets/logo-pdf.png");
        logo.setWidthPercentage(5);
        logo.scaleAbsoluteWidth(150);
        logo.scaleAbsoluteHeight(80);
        logo.setAlignment(Element.ALIGN_CENTER);

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
        intervalTransactions.stream().sorted(idComparator.reversed()).forEach(transaction -> {
            PdfPCell c1 = new PdfPCell(new Phrase(transaction.getId() + ""));
            c1.setBackgroundColor(new Color(165, 144, 234));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);


            PdfPCell c2 = new PdfPCell(new Phrase(transaction.getType() + ""));
            c2.setBackgroundColor(new Color(165, 144, 234));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase(transaction.getDescription()));
            c3.setBackgroundColor(new Color(165, 144, 234));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c3);


//            table.addCell(transaction.getId() + "");
//            table.addCell(transaction.getType() + "");
//            table.addCell(transaction.getDescription());
            String amount = transaction.getType() == TransactionType.DEBIT ? "-" + transaction.getAmount() : transaction.getAmount() + "";
//            table.addCell(amount);

            PdfPCell c4 = new PdfPCell(new Phrase(amount));
            c4.setBackgroundColor(new Color(165, 144, 234));
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c4);


            String hours = transaction.getDate().getHour() > 9 ? transaction.getDate().getHour() + "" : "0" + transaction.getDate().getHour();
            String minutes = transaction.getDate().getMinute() > 9 ? transaction.getDate().getMinute() + "" : "0" + transaction.getDate().getMinute();

            String date = transaction.getDate().getYear() + "-" +
                    transaction.getDate().getMonthValue() + "-" +
                    transaction.getDate().getDayOfMonth() + " " +
                    hours + ":" + minutes;

            PdfPCell c5 = new PdfPCell(new Phrase(date));
            c5.setBackgroundColor(new Color(165, 144, 234));
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c5);

//            table.addCell(date);

            PdfPCell c6 = new PdfPCell(new Phrase(transaction.getBalance() + ""));
            c6.setBackgroundColor(new Color(165, 144, 234));
            c6.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c6);
//            table.addCell(transaction.getBalance() + "");
        });

        document.add(logo);
        document.add(paragraph);
        document.add(table);
        document.close();
    }


}
