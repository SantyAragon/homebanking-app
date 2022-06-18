package com.mindhub.HomeBanking.services.implement;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.HomeBanking.models.Account;
import com.mindhub.HomeBanking.models.Client;
import com.mindhub.HomeBanking.models.Transaction;
import com.mindhub.HomeBanking.services.AccountService;
import com.mindhub.HomeBanking.services.ClientService;
import com.mindhub.HomeBanking.services.PdfGenerator;
import com.mindhub.HomeBanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);


        //NEW TABLE
        PdfPTable table = new PdfPTable(5);


        //HEADERS
        java.util.List<String> miLista = List.of("ID", "Description", "Amount", "Date", "Balance");


        miLista.forEach(element -> {
            PdfPCell c1 = new PdfPCell(new Phrase(element));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        });
        //CELLS FOR EACH TRANSACTIONS
        transactions.forEach(transaction -> {
            table.addCell(transaction.getId() + "");
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getAmount() + "");
            table.addCell(transaction.getDate() + "");
            table.addCell(transaction.getBalance() + "");
        });


        document.add(paragraph);
        document.add(table);
        document.close();
    }


}
