package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.OutputStream;
import java.util.Map;

@Service
public class Html2PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    public OutputStream template2Pdf(String templateName, Map parameters) {
//        OutputStream outputStream = new BufferedOutputStream();
//        IOUtils.copy()
//
//        Context ctx = new Context();
//        String processedHtml = templateEngine.process(templateName, ctx);
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(processedHtml);
//        renderer.layout();
//        renderer.createPDF(os, false);
//        renderer.finishPDF();
        return null;
    }

    public String template2Html(String templateName, Patient patient) {
        Context ctx = new Context();
        ctx.setVariable("patient", patient);
        String processedHtml = templateEngine.process(templateName, ctx);
        return processedHtml;
    }
}