package com.tutor.ai.service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.tutor.ai.model.Mensagem;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class PdfExportService {
	
	public static String exportarHistoricoParaPdf(List<Mensagem> historico) {

		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nomeArquivo = "Historico_Tutor_IA_" + timestamp + ".pdf";

        Document documento = new Document();

        try {

        	// Configuração de Fontes Universais
        	Font fonteTitulo = new Font(Font.HELVETICA, 18, Font.BOLD);
        	Font fonteSubtitulo = new Font(Font.HELVETICA, 10, Font.ITALIC);
            Font fonteTexto = new Font(Font.HELVETICA, 12, Font.NORMAL);

            // Cabeçalho do Relatório
            documento.add(new Paragraph("Java AI Tutor - Relatório de Mentoria", fonteTitulo));
            documento.add(new Paragraph("Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n", fonteSubtitulo));

            if (historico.isEmpty()) {
                documento.add(new Paragraph("Nenhum registro de conversa encontrado no histórico.", fonteTexto));
            } else {
                for (Object obj : historico) {
                    Mensagem msg = (Mensagem) obj;
                    String blocoTexto = String.format("[%s] %s:\n%s\n\n", 
                        msg.getDataHora(), 
                        msg.getRemetente().toUpperCase(), 
                        msg.getMensagem()
                    );
                    documento.add(new Paragraph(blocoTexto, fonteTexto));
                }
            }

            return nomeArquivo;
        } catch (Exception e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
            return null;
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
	}
}
