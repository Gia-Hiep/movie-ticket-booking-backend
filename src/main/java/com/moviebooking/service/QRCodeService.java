package com.moviebooking.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.moviebooking.dto.TicketDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class QRCodeService {

    public String generateQRCode(TicketDTO ticketDTO) {
        String qrContent = String.format("Ticket ID: %d, User ID: %d, Showtime ID: %d, Amount: %.2f",
                ticketDTO.getId() != null ? ticketDTO.getId() : 0,
                ticketDTO.getUserId(),
                ticketDTO.getShowtimeId(),
                ticketDTO.getTotalAmount());
        int width = 300;
        int height = 300;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

            // Lưu hình ảnh QR vào hệ thống tệp
            String fileName = UUID.randomUUID().toString() + ".png";
            Path filePath = Path.of("qrcodes/" + fileName);
            Files.createDirectories(filePath.getParent());
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

            // Trả về URL của hình ảnh
            return "http://localhost:8080/qrcodes/" + fileName;
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage(), e);
        }
    }
}