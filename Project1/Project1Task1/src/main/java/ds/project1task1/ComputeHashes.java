package ds.project1task1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/05/2024
 */
@WebServlet(name = "ComputeHashes", urlPatterns = {"/ComputeHashes"})
public class ComputeHashes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String textData = req.getParameter("textData");
        String hashFunction = req.getParameter("hashFunction");

        String hexHash = "";
        String base64Hash = "";

        try {
            // Compute the hash value
            MessageDigest digest = MessageDigest.getInstance(hashFunction);
            byte[] hashedBytes = digest.digest(textData.getBytes("UTF-8"));

            // Convert to Hex and Base64
            hexHash = DatatypeConverter.printHexBinary(hashedBytes);
            base64Hash = DatatypeConverter.printBase64Binary(hashedBytes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Set the content type to HTML as the response will be in HTML
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // Write the HTML response
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hash Results</title>");
        // Import Bootstrap CSS
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>"); // 添加视口元标签
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container mt-3'>");
        out.println("<h2>Hash Results</h2>");
        out.println("<p>Original Text: <strong>" + textData + "</strong></p>");
        out.println("<p>Hash Function: <strong>" + hashFunction + "</strong></p>");
        out.println("<p>Hexadecimal Hash: <strong>" + hexHash + "</strong></p>");
        out.println("<p>Base64 Hash: <strong>" + base64Hash + "</strong></p>");
        out.println("<button type='button' class='btn btn-primary btn-lg' onclick='goBack()'>Go Back</button>"); // 使用btn-lg类来增大按钮
        out.println("</div>");
        out.println("<script src='https://code.jquery.com/jquery-3.5.1.slim.min.js'></script>");
        out.println("<script src='https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js'></script>");
        out.println("<script src='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js'></script>");
        out.println("<script>function goBack() { window.history.back(); }</script>");
        out.println("</body>");
        out.println("</html>");
    }
}
